package com.yunlsp.saas.service.api.portal.utils;

import com.yunlsp.saas.service.api.model.UserModel;
import com.yunlsp.saas.service.api.portal.constant.Conf;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * SsoLoginHelper
 *
 * @author rodxu
 * @date 2017/11/28.
 */
public class LoginHelper {

    private static Logger logger = LoggerFactory.getLogger(LoginHelper.class);

    /**
     *
     */
    private static final String COOKIE_DOMAIN = null;

    /**
     * load cookie sessionId
     *
     * @param request
     * @return
     */
    public static String cookieSessionId(HttpServletRequest request) {
        return CookieUtils.getValue(request, Conf.SSO_SESSIONID);
    }

    /**
     * @param response
     * @param sessionId
     */
    public static void cookieSessionIdSet(HttpServletResponse response, String sessionId) {
        cookieSessionIdSet(response, sessionId, COOKIE_DOMAIN);
    }

    public static void cookieSessionIdSet(HttpServletResponse response, String sessionId, String domain) {
        if (sessionId != null && sessionId.trim().length() > 0) {
            CookieUtils.set(response, Conf.SSO_SESSIONID, sessionId, domain, false);
        }
    }

    public static void cookieSessionIdRemove(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.remove(request, response, Conf.SSO_SESSIONID);
    }

    public static UserModel loginCheck(HttpServletRequest request) {
        return loginCheck(request, UserModel.class);
    }

    /**
     * login check
     *
     * @param request
     * @return
     */
    public static <T extends Serializable> T loginCheck(HttpServletRequest request, Class<T> clazz) {
        String cookieSessionId = cookieSessionId(request);
        if (StringUtils.isNotBlank(cookieSessionId)) {
            return loginCheck(cookieSessionId, clazz);
        }
        logger.warn("获取用户信息失败：未获取到带sso_sessionId的cookie值");
        return null;
    }

    public static UserModel loginCheck(String sessionId) {
        return loginCheck(sessionId, UserModel.class);
    }

    /**
     * login check
     *
     * @param sessionId
     * @return
     */
    public static <T extends Serializable> T loginCheck(String sessionId, Class<T> clazz) {
        if (StringUtils.isNotBlank(sessionId)) {
            try {
                T user = UserLoginStore.getLoginUserInfo(sessionId, clazz);
                if (user != null) {
                    return user;
                }
            } catch (Throwable t) {
                logger.error(String.format("redis获取用户信息异常：%s", t.getMessage()), t);
            }
        }
        logger.warn("获取用户信息失败：sessionId:{}", sessionId);
        return null;
    }

    /**
     * client login
     *
     * @param response
     * @param sessionId
     */
    public static void login(HttpServletResponse response, String sessionId, UserModel user, boolean isRememberMe) {
        try {
            UserLoginStore.put(sessionId, user, isRememberMe);
            CookieUtils.set(response, Conf.SSO_SESSIONID, sessionId, isRememberMe);
        } catch (Throwable t) {
            logger.error(String.format("redis写入值异常：%s", t.getMessage()), t);
        }
    }

    /**
     * client logout
     *
     * @param request
     * @param response
     */
    public static void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String cookieSessionId = cookieSessionId(request);
            if (logger.isInfoEnabled()) {
                logger.info("logout,cookieSessionId:" + cookieSessionId);
            }
            if (cookieSessionId != null) {
                //清redis
                UserLoginStore.remove(cookieSessionId);
            }
            //清cookie
            CookieUtils.remove(request, response, Conf.SSO_SESSIONID);
        } catch (Throwable t) {
            logger.error(String.format("退出删除redis异常：%s", t.getMessage()), t);
        }
    }
}
