package com.yunlsp.saas.service.api.portal.auth;

import com.yunlsp.saas.service.api.model.UserModel;
import com.yunlsp.saas.service.api.portal.utils.LoginHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 新的用户实体信息--支持多公司
 *
 * @author 许路路
 */
public class Principal implements IPrincipal {

    private static Logger logger = LoggerFactory.getLogger(Principal.class);

    @Override
    public Object getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        UserModel user = LoginHelper.loginCheck(request);
        return user;
    }
}
