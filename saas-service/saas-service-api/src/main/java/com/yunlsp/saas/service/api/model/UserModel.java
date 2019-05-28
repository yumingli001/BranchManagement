package com.yunlsp.saas.service.api.model;

import java.io.Serializable;

/**
 * @author 许路路
 */
public class UserModel implements Serializable {

    private static final long serialVersionUID = -787138888888865868L;

    public UserModel() {
    }

    public UserModel(String userId, String phone) {
        this.userId = userId;
        this.phone = phone;
    }

    private UserModel(Builder builder) {
        this.userId = builder.userId;
        this.phone = builder.phone;
        this.wechatOpenId = builder.wechatOpenId;
        this.userName = builder.userName;
        this.avatar = builder.avatar;
        this.companyId = builder.companyId;
        this.isAdmin = builder.isAdmin;
    }

    public static Builder newBuilder(String userId, String phone) {
        return new Builder(userId, phone);
    }

    public static class Builder {

        private String userId;
        private String phone;
        private String wechatOpenId;
        private String userName;
        private String avatar;
        private String companyId;
        private boolean isAdmin;

        public Builder(String userId, String phone) {
            this.userId = userId;
            this.phone = phone;
        }

        public Builder companyId(String companyId) {
            this.companyId = companyId;
            return this;
        }

        public Builder isAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder wechatOpenId(String wechatOpenId) {
            this.wechatOpenId = wechatOpenId;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserModel builder() {
            return new UserModel(this);
        }

    }

    /**
     * 用户的唯一标识
     */
    private String userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 微信openid
     */
    private String wechatOpenId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 公司标识
     */
    private String companyId;

    /**
     * 是否公司的管理员
     */
    public boolean isAdmin;

    public String getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechatOpenId() {
        return wechatOpenId;
    }

    public void setWechatOpenId(String wechatOpenId) {
        this.wechatOpenId = wechatOpenId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

}
