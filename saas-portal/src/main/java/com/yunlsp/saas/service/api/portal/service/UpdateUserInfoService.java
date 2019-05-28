package com.yunlsp.saas.service.api.portal.service;

import com.yunlsp.saas.service.api.UserInfoService;
import com.yunlsp.saas.service.api.model.Permission;
import com.yunlsp.saas.service.api.model.UserDetailModel;
import com.yunlsp.saas.service.api.model.UserModel;
import com.yunlsp.saas.service.api.portal.utils.LoginHelper;
import com.yunlsp.saas.service.api.portal.utils.UserLoginStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息变更服务
 *
 * @author Rod.xu
 */
@Component
public class UpdateUserInfoService {

    private static final Logger logger = LoggerFactory.getLogger("UpdateUserInfoService");

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 刷新redis中的用户信息
     *
     * @param ssoSessionId
     */
    @Async
    public void updateUserInfo(String ssoSessionId) {
        logger.debug("异步刷新用户信息:{}", ssoSessionId);
        UserModel user = LoginHelper.loginCheck(ssoSessionId);
        if (user != null) {
            UserDetailModel userDetail = userInfoService.userDetailInfo(user.getUserId());
            if (userDetail != null) {
                user = userDetail.toUserModel();
                UserLoginStore.put(ssoSessionId, user, false);
            }
        }
    }

    /**
     * 刷新redis中的用户权限信息
     *
     * @param userId
     * @return
     */
    @Async
    public void updateUserPerm(String userId) {
        logger.debug("异步刷新用户的权限信息:{}", userId);
        //TODO 获取用户的权限-
        List<Permission> userPerms = new ArrayList();
        userPerms.add(new Permission("all", "/**"));
        //用户权限存redis
        UserLoginStore.putPerm(userId, userPerms);
    }

}
