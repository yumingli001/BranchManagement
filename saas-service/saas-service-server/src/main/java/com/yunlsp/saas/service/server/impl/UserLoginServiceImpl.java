package com.yunlsp.saas.service.server.impl;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.yunlsp.common.RandomUtils;
import com.yunlsp.common.domain.Result;
import com.yunlsp.common.encryption.Md5Utils;
import com.yunlsp.common.exception.ParamException;
import com.yunlsp.saas.service.api.UserLoginService;
import com.yunlsp.saas.service.api.model.UserDetailModel;
import com.yunlsp.saas.service.api.model.UserModel;
import com.yunlsp.saas.service.bean.SaasUser;
import com.yunlsp.saas.service.dao.SaasUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: 许路路
 * @Date: 2019/3/5
 */
@Service("userLoginService")
public class UserLoginServiceImpl implements UserLoginService {

    @NacosValue(value = "${test:1234}", autoRefreshed = true)
    private String nacosValue;

    @Autowired
    private SaasUserMapper userMapper;

    @Override
    public String sayHi(String name) {
        return "hi:" + name + ":" + nacosValue;
    }

    @Override
    public Result loginCheck(String account, String password) {
        SaasUser user = userMapper.selectByPhone(account);
        if (user == null) {
            return Result.builder(false).msg("账号不存在").build();
        }
        String userId = user.getUserId();
        if (!pswCheck(password, user.getPswSalt(), user.getPassword())) {
            return Result.builder(false).msg("账号密码不匹配").code(400).build();
        }
        if (user.getAccountExpiryDate().before(new Date())) {
            return Result.builder(false).msg("账号已过期").build();
        }
        UserDetailModel detailModel = userMapper.selectUserInfoByUserId(userId);
        UserModel userModel = detailModel.toUserModel();
        return Result.builder(true).msg(nacosValue).T(userModel).build();
    }

    @Override
    public void resetPassword(String phone, String newPassword) throws ParamException {
        SaasUser user = userMapper.selectByPhone(phone);
        if (user == null) {
            throw new ParamException("账号不存在:" + phone);
        }
        if (StringUtils.isBlank(newPassword) || newPassword.length() < 6) {
            throw new ParamException("新密码长度不符合要求");
        }
        String salt = RandomUtils.randomCharStr(8);
        String psw = Md5Utils.getMD5(Md5Utils.getMD5(salt + newPassword + salt));
        user.setPswSalt(salt);
        user.setPassword(psw);
        user.setModifyTime(new Date());
        userMapper.update(user);
    }

    private boolean pswCheck(String psw, String salt, String enPsw) {
        return enPsw.equals(Md5Utils.getMD5(Md5Utils.getMD5(salt + psw + salt)));
    }
}
