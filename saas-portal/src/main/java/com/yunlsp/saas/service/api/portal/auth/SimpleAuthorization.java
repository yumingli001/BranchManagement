package com.yunlsp.saas.service.api.portal.auth;

import javax.servlet.http.HttpServletRequest;

/**
 * 简单权限认证，为类似网站只需要登陆的不需要权限的应用使用
 *
 * @author 许路路
 */
public class SimpleAuthorization implements IAuthorization {

    @Override
    public boolean hasAuth(HttpServletRequest request, Object currentUser, Object handler) {
        if (currentUser != null) {
            return true;
        }
        return false;
    }

}
