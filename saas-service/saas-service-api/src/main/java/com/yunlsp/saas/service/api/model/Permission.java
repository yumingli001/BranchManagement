package com.yunlsp.saas.service.api.model;

import java.io.Serializable;

/**
 * @author 许路路
 */
public class Permission implements Serializable {

    private String permIdentity;

    private String permUrl;

    public Permission() {
    }

    public Permission(String permIdentity, String permUrl) {
        this.permIdentity = permIdentity;
        this.permUrl = permUrl;
    }

    public String getPermIdentity() {
        return permIdentity;
    }

    public void setPermIdentity(String permIdentity) {
        this.permIdentity = permIdentity;
    }

    public String getPermUrl() {
        return permUrl;
    }

    public void setPermUrl(String permUrl) {
        this.permUrl = permUrl;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Permission{");
        sb.append("permIdentity='").append(permIdentity).append('\'');
        sb.append(", permUrl='").append(permUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
