package com.yunlsp.saas.service.api.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: 许路路
 * @Date: 2019/3/5
 */
public class LoginModel implements Serializable {

    private String loginName;

    private String password;

    private boolean rememberMe;

    private String returnUrl;

    private Map<String, String> param;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LoginModel{");
        sb.append("loginName='").append(loginName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", rememberMe=").append(rememberMe);
        sb.append(", returnUrl='").append(returnUrl).append('\'');
        sb.append(", param=").append(param);
        sb.append('}');
        return sb.toString();
    }

}
