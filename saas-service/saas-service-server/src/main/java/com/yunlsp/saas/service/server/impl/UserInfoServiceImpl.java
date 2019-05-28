package com.yunlsp.saas.service.server.impl;

import com.yunlsp.common.RandomUtils;
import com.yunlsp.common.encryption.Md5Utils;
import com.yunlsp.common.exception.ParamException;
import com.yunlsp.saas.service.api.UserInfoService;
import com.yunlsp.saas.service.api.model.UserDetailModel;
import com.yunlsp.saas.service.api.model.UserModifyModel;
import com.yunlsp.saas.service.bean.SaasUser;
import com.yunlsp.saas.service.dao.SaasUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: 许路路
 * @Date: 2019/3/7
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    private static final int password_min_length = 6;

    @Autowired
    private SaasUserMapper userMapper;

    @Override
    public void modifyUserInfo(UserModifyModel modifyModel) throws ParamException {
        SaasUser user = getUserInfo(modifyModel.getUserId());
        user.setUserName(modifyModel.getName());
        user.setAvatar(modifyModel.getImage());
        user.setEmail(modifyModel.getEmail());
        update(user);
    }

    @Override
    public void modifyPhone(String userId, String newPhone) throws ParamException {
        SaasUser user = getUserInfo(userId);
        user.setPhone(newPhone);
        update(user);
    }

    @Override
    public void modifyPassword(String userId, String oldPsw, String newPsw) throws ParamException {
        SaasUser user = getUserInfo(userId);
        if (!user.getPassword().equals(getEnPassword(oldPsw, user.getPswSalt()))) {
            throw new ParamException("原始密码不正确");
        }
        if (StringUtils.isBlank(newPsw) || newPsw.length() < password_min_length) {
            throw new ParamException("密码长度必须大于等于6位");
        }
        String salt = RandomUtils.randomCharStr(8);
        user.setPswSalt(salt);
        user.setPassword(getEnPassword(newPsw, salt));
        update(user);
    }

    @Override
    public boolean phoneIsExsit(String phone) {
        SaasUser user = userMapper.selectByPhone(phone);
        return user != null;
    }

    @Override
    public UserDetailModel userDetailInfo(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        return userMapper.selectUserInfoByUserId(userId);
    }

    private SaasUser getUserInfo(String userId) throws ParamException {
        SaasUser user = userMapper.selectByUserId(userId);
        if (user == null) {
            throw new ParamException("用户不存在:" + userId);
        }
        return user;
    }

    private void update(SaasUser user) {
        user.setModifyTime(new Date());
        userMapper.update(user);
    }

    private String getEnPassword(String password, String salt) {
        return Md5Utils.getMD5(Md5Utils.getMD5(salt + password + salt));
    }
}
