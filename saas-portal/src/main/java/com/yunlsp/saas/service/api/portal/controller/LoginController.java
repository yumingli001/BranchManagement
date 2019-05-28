package com.yunlsp.saas.service.api.portal.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.yunlsp.common.domain.Result;
import com.yunlsp.common.exception.ParamException;
import com.yunlsp.common.validate.HibernateParamValidateUtils;
import com.yunlsp.saas.service.api.UserLoginService;
import com.yunlsp.saas.service.api.model.ForgetPassword;
import com.yunlsp.saas.service.api.model.LoginModel;
import com.yunlsp.saas.service.api.model.UserModel;
import com.yunlsp.saas.service.api.portal.constant.Conf;
import com.yunlsp.saas.service.api.portal.service.UpdateUserInfoService;
import com.yunlsp.saas.service.api.portal.utils.JedisUtil;
import com.yunlsp.saas.service.api.portal.utils.LoginHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Author: 许路路
 * @Date: 2019/3/5
 */
@Api(description = "登录接口", tags = {"user login"})
@RestController
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserLoginService loginService;

    @Autowired
    private UpdateUserInfoService updateUserInfoService;

    /**
     * 登录时间间隔
     */
    @NacosValue("${login.interval}")
    private Integer loginInterval;

    /**
     * 错误次数限制
     */
    @NacosValue("${login.pwd.error.limit}")
    private Integer errorlimit;

    /**
     * 账户锁定时长
     */
    @NacosValue("${login.lock.time}")
    private Integer lockTime;

    private static final int password_min_length = 6;

    @ApiOperation(value = "登录", notes = "网页端登录接口")
    @ResponseBody
    @RequestMapping(value = "login", produces = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public Result login(HttpServletRequest request, HttpServletResponse response,
                        @RequestBody LoginModel loginModel) {
        //检查是否已经登录过
        UserModel userModel = LoginHelper.loginCheck(request);
        if (userModel != null) {
            String sessionId = LoginHelper.cookieSessionId(request);
            return Result.builder(true).msg("success").T(sessionId).build();
        }

        // 检查账户是否被锁定
        String phone = loginModel.getLoginName();
        String lockKey = Conf.LOGIN_USER_BLACKLIST_KEY + phone;
        // 已经被锁定的手机号
        String lockedPhone = JedisUtil.getStringValue(lockKey);
        if (StringUtils.isNotBlank(lockedPhone)) {
            return Result.builder(false).msg("账户未解锁").build();
        }

        //登录账号密码验证
        Result res = loginService.loginCheck(loginModel.getLoginName(), loginModel.getPassword());
        if (!res.getSuccess()) {
            // 密码输入错误的情况
            if (res.getCode() == 400) {
                return passwordErrorProcess(res, lockKey, phone);
            }
            return res;
        }

        userModel = (UserModel) res.getData();
        //登陆成功--用户信息写redis--权限写redis
        String sessionId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        LoginHelper.login(response, sessionId, userModel, loginModel.isRememberMe());
        updateUserInfoService.updateUserPerm(userModel.getUserId());
        return Result.builder(true).msg("success").T(sessionId).build();
    }

    /**
     * Logout
     */
    @ApiOperation(value = "退出登录", notes = "退出登录")
    @RequestMapping(value = "logout", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        LoginHelper.logout(request, response);
        return Result.builder(true).msg("success").build();
    }

    @ApiOperation(value = "忘记密码", notes = "忘记密码(未登陆)")
    @PostMapping("forgetPassword")
    public Result forgetPassword(HttpServletRequest request, @RequestBody ForgetPassword forgetPassword) {
        //参数验证
        try {
            HibernateParamValidateUtils.validate(forgetPassword);
        } catch (ParamException e) {
            logger.error(e.getMessage(), e);
            return Result.builder(false).code(400).msg(e.getMessage()).build();
        }
        if (forgetPassword.getNewPassword().length() < password_min_length || !forgetPassword.getNewPassword().equals(forgetPassword.getConfirmPassword())) {
            return Result.builder(false).code(400).msg("密码长度不足6位或两次密码输入不一致").build();
        }
        //验证码验证
        Result returnT = CommonController.messageCodeValidate(request, forgetPassword.getMessageCode(), forgetPassword.getPhone());
        if (!returnT.getSuccess()) {
            return returnT;
        }
        //修改密码
        try {
            String phone = forgetPassword.getPhone();
            loginService.resetPassword(phone, forgetPassword.getNewPassword());

            // 重置密码后删除 redis 里锁定的账户
            JedisUtil.del(Conf.LOGIN_USER_BLACKLIST_KEY + phone);

        } catch (ParamException e) {
            return Result.builder(false).code(400).msg(e.getMessage()).build();
        }
        request.getSession().removeAttribute(Conf.MESSAGE_CODE_SESSION_KEY);

        return Result.builder(true).msg("success").build();
    }

    /**
     * 处理密码错误的情况
     *
     * @param res
     * @param lockedKey
     * @param phone
     * @return
     */
    private Result passwordErrorProcess(Result res, String lockedKey, String phone) {
        String errorCountKey = Conf.PSW_INPUT_ERROR_COUNT_KEY + phone;
        String errorCountForStr = JedisUtil.getStringValue(errorCountKey);
        if (StringUtils.isNotBlank(errorCountForStr)) {
            // 错误次数加 1
            Long incrResult = JedisUtil.incrBy(errorCountKey, 1);
            if (incrResult > errorlimit) {
                // 锁定账户(这里 key 和 value 都是 phone)
                JedisUtil.setStringValue(lockedKey, phone, lockTime);

                // 删除错误次数
                JedisUtil.del(errorCountKey);
                return Result.builder(false).msg("账户已被锁定,请30分钟后重试").build();
            }
        } else {
            JedisUtil.setStringValue(errorCountKey, String.valueOf(1), loginInterval);
        }
        return res;
    }
}
