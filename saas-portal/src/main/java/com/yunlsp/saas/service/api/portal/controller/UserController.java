package com.yunlsp.saas.service.api.portal.controller;

import com.yunlsp.common.domain.Result;
import com.yunlsp.common.exception.ParamException;
import com.yunlsp.saas.service.api.UserInfoService;
import com.yunlsp.saas.service.api.model.UserDetailModel;
import com.yunlsp.saas.service.api.model.UserModel;
import com.yunlsp.saas.service.api.model.UserModifyModel;
import com.yunlsp.saas.service.api.portal.annotation.UpdateUserInfo;
import com.yunlsp.saas.service.api.portal.constant.Conf;
import com.yunlsp.saas.service.api.portal.service.UpdateUserInfoService;
import com.yunlsp.saas.service.api.portal.utils.LoginHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 许路路
 * @Date: 2019/3/7
 */
@Api(description = "用户信息接口", tags = {"user info"})
@RestController
@RequestMapping("user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UpdateUserInfoService updateUserInfoService;

    @GetMapping("/baseInfo")
    public Result baseInfo(HttpServletRequest request) {
        UserModel userModel = (UserModel) request.getAttribute(Conf.SSO_USER);
        if (StringUtils.isBlank(userModel.getUserName())) {
            userModel.setUserName(userModel.getPhone());
        }
        return Result.builder(true).msg("success").T(userModel).build();
    }

    @ApiOperation(value = "获取用户的详细信息", notes = "用户的详细信息")
    @GetMapping("/detailInfo")
    public Result detailInfo(HttpServletRequest request) {
        UserModel userModel = (UserModel) request.getAttribute(Conf.SSO_USER);
        UserDetailModel detailModel = userInfoService.userDetailInfo(userModel.getUserId());
        if (detailModel == null) {
            return Result.builder(false).msg("用户不存在").build();
        }
        return Result.builder(true).msg("success").T(detailModel).build();
    }

    @UpdateUserInfo
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @PostMapping("modifyUserInfo")
    public Result modifyUserInfo(HttpServletRequest request, @RequestBody UserModifyModel modifyModel) {
        UserModel userModel = (UserModel) request.getAttribute(Conf.SSO_USER);
        modifyModel.setUserId(userModel.getUserId());
        try {
            userInfoService.modifyUserInfo(modifyModel);
        } catch (ParamException e) {
            return Result.builder(false).code(400).msg(e.getMessage()).build();
        }
        //更新redis中的用户信息
        updateUserInfoService.updateUserInfo(LoginHelper.cookieSessionId(request));
        return Result.builder(true).msg("success").build();
    }

    @UpdateUserInfo
    @ApiOperation(value = "修改手机号码", notes = "修改手机号码")
    @PostMapping("modifyPhone")
    public Result modifyPhone(HttpServletRequest request, String newPhone, String msgCode, String key) {
        //原手机的验证码
        Result returnT = CommonController.sessionValidate(request, Conf.MESSAGE_CODE_SUCCCESS_IDENTIFY, key);
        if (!returnT.getSuccess()) {
            returnT.setMsg("失败：原手机短信验证码未验证,请重新操作");
            return returnT;
        }
        //验证码验证
        returnT = CommonController.messageCodeValidate(request, msgCode, newPhone);
        if (!returnT.getSuccess()) {
            return returnT;
        }
        UserModel userModel = (UserModel) request.getAttribute(Conf.SSO_USER);
        if (userInfoService.phoneIsExsit(newPhone)) {
            return Result.builder(false).code(400).msg("新手机号" + newPhone + "已被注册,不能修改").build();
        }
        logger.info("用户{}修改手机号,原手机号:{},新手机号:{}.", userModel.getUserId(), userModel.getPhone(), newPhone);
        try {
            userInfoService.modifyPhone(userModel.getUserId(), newPhone);
        } catch (ParamException e) {
            return Result.builder(false).code(400).msg(e.getMessage()).build();
        }
        //更新redis中的用户信息
        updateUserInfoService.updateUserInfo(LoginHelper.cookieSessionId(request));
        return Result.builder(true).msg("success").build();
    }

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PostMapping("modifyPassword")
    public Result modifyPassword(HttpServletRequest request, String oldPassword, String newPassword) {
        UserModel userModel = (UserModel) request.getAttribute(Conf.SSO_USER);
        try {
            userInfoService.modifyPassword(userModel.getUserId(), oldPassword, newPassword);
        } catch (ParamException e) {
            return Result.builder(false).code(400).msg(e.getMessage()).build();
        }
        return Result.builder(true).msg("success").build();
    }
}
