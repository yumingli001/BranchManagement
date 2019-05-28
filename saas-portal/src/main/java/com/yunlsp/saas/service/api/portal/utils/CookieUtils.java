package com.yunlsp.saas.service.api.portal.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CookieUtil
 *
 * @author rodxu
 * @date 2018/4/31
 */
public class CookieUtils {

    /**
     * 记住我缓存时间,单位/秒, 14Days
     */
    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 14;

    /**
     * 存储路径
     */
    private static final String COOKIE_PATH = "/";

    /**
     * cookie domain
     */
    private static final String COOKIE_DOMAIN = null;

    /**
     * 保存
     *
     * @param response
     * @param key
     * @param value
     * @param ifRemember
     */
    public static void set(HttpServletResponse response, String key, String value, boolean ifRemember) {
        int age = ifRemember ? COOKIE_MAX_AGE : -1;
        set(response, key, value, COOKIE_DOMAIN, COOKIE_PATH, age, true);
    }

    /**
     * 保存
     *
     * @param response
     * @param key
     * @param value
     * @param domain
     * @param ifRemember
     */
    public static void set(HttpServletResponse response, String key, String value, String domain, boolean ifRemember) {
        int age = ifRemember ? COOKIE_MAX_AGE : -1;
        set(response, key, value, domain, COOKIE_PATH, age, true);
    }

    /**
     * 保存
     *
     * @param response
     * @param key
     * @param value
     * @param maxAge
     */
    private static void set(HttpServletResponse response, String key, String value, String domain, String path, int maxAge, boolean isHttpOnly) {
        Cookie cookie = new Cookie(key, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }

    /**
     * 查询value
     *
     * @param request
     * @param key
     * @return
     */
    public static String getValue(HttpServletRequest request, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 查询Cookie
     *
     * @param request
     * @param key
     */
    private static Cookie get(HttpServletRequest request, String key) {
        Cookie[] arrCookie = request.getCookies();
        if (arrCookie != null && arrCookie.length > 0) {
            for (Cookie cookie : arrCookie) {
                if (cookie.getName().equals(key)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 删除Cookie
     *
     * @param request
     * @param response
     * @param key
     */
    public static void remove(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            //清除cookie
            set(response, key, "", null, COOKIE_PATH, 0, true);
        }
    }

}