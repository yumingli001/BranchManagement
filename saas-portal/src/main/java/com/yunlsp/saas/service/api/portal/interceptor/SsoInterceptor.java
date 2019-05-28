package com.yunlsp.saas.service.api.portal.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.yunlsp.common.domain.Result;
import com.yunlsp.saas.service.api.model.UserModel;
import com.yunlsp.saas.service.api.portal.annotation.UpdateUserInfo;
import com.yunlsp.saas.service.api.portal.auth.*;
import com.yunlsp.saas.service.api.portal.constant.Conf;
import com.yunlsp.saas.service.api.portal.enums.ErrorCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 登陆&权限拦截器
 *
 * @author 许路路
 */
public class SsoInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(SsoInterceptor.class);

    private IAuthorization authorization = new SimpleAuthorization();
    private IPrincipal principal = new Principal();

    private Method _method;
    private boolean _is_callback;
    private String _servlet_path;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        addAcross(request, response);
        _is_callback = StringUtils.isNotBlank(request.getParameter("callback"));
        _servlet_path = request.getServletPath();
        //用户退出
        if ("/logout".equals(_servlet_path)) {
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }
        //当前登陆用户
        Object _current_user = principal.getCurrentUser(request, response);

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        _method = handlerMethod.getMethod();
        //未登录处理
        if (_current_user == null) {
            jsonOutput(ErrorCodeEnum.NO_LOGIN, request, response);
            return false;
        }
        //已登陆用户,判断是否有权限
        if (!authorization.hasAuth(request, _current_user, handler)) {
            //没有权限
            jsonOutput(ErrorCodeEnum.NO_AUTHORITY, request, response);
            return false;
        }
        //认证通过,当前用户信息加入当前请求
        request.setAttribute(Conf.SSO_USER, _current_user);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }
        //注解
        UpdateUserInfo userInfoAnnotation = AuthHelper.isHasAnnotation(_method.getDeclaredAnnotations(), UpdateUserInfo.class);
        if (userInfoAnnotation == null) {
            return;
        }
        //有这个注解的  redis用户信息变更列表中添加改用户id
        UserModel user = (UserModel) request.getAttribute(Conf.SSO_USER);
        logger.info("有需要用户信息更新的变更:{}", user.getUserId());
        //SsoUserStore.setChangedUser(user.getUserId());
        //EventPublisher.publishEvent(new UserInfoChangedEvent(this, user.getUserId()));
    }

    private void jsonOutput(ErrorCodeEnum errorCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Result result = Result.builder(false).code(errorCode.getCode()).msg(errorCode.getMsg()).build();
        if (_is_callback) {
            String output = request.getParameter("callback") + "(" + JSONObject.toJSONString(result) + ")";
            response.setContentType("text/javascript");
            response.getWriter().print(output);
        } else {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSONObject.toJSONString(result));
        }
    }

    public void set_authorization(IAuthorization _authorization) {
        if (_authorization != null) {
            this.authorization = _authorization;
            logger.info("认证接口自定义初始化完成:" + _authorization.getClass().getName());
        }
    }

    public void setPrincipal(IPrincipal principal) {
        if (principal != null) {
            this.principal = principal;
            logger.info("认证接口设置认证主体方法:" + principal.getClass().getName());
        }
    }

    private void addAcross(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
    }
}
