package com.yunlsp.saas.service.api.portal.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户主体
 *
 * @author 许路路
 */
public interface IPrincipal {

    /**
     * 获取当前登陆的用户
     *
     * @param request
     * @param response
     * @return
     */
    Object getCurrentUser(HttpServletRequest request, HttpServletResponse response);

}
