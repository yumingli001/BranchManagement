package com.yunlsp.saas.service.api.portal.auth;

import javax.servlet.http.HttpServletRequest;


/**
 * 授权接口
 *
 * @author 许路路
 */
public interface IAuthorization {

    /**
     * 是否有权限
     *
     * @param request
     * @param currentUser
     * @param handler
     * @return
     */
    boolean hasAuth(HttpServletRequest request, Object currentUser, Object handler);
}
