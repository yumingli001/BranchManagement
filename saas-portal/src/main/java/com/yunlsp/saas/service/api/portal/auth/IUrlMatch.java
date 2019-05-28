package com.yunlsp.saas.service.api.portal.auth;

/**
 * @author 许路路
 */
public interface IUrlMatch {

    /**
     * url 匹配
     *
     * @param matchUrl
     * @param reqUrl
     * @return
     */
    boolean match(String matchUrl, String reqUrl);
}
