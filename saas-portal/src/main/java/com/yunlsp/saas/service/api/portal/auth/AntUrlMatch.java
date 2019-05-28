package com.yunlsp.saas.service.api.portal.auth;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * ANT方式的通配符URL匹配
 * <p>
 * 有三种： ?（匹配任何单字符），（匹配0或者任意数量的字符），*（匹配0或者更多的目录）
 *
 * @author 许路路
 */
public class AntUrlMatch implements IUrlMatch {

    @Override
    public boolean match(String urlPattern, String reqUrl) {
        PathMatcher matcher = new AntPathMatcher();
        if (matcher.match(urlPattern, reqUrl)) {
            return true;
        }
        return false;
    }
}
