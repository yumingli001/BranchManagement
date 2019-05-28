package com.yunlsp.saas.service.api.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: 许路路
 * @Date: 2019/3/12
 */
public class UserDetailModel implements Serializable {

    /**
     * 用户的唯一标识
     */
    private String userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户账号状态 0 正常  2无效
     */
    private int status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 账号过期时间
     */
    private Date expiryDate;

    /**
     * 邮箱
     */
    private String email;

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
     * 公司主键id
     */
    private int companyKey;

    /**
     * 公司标识
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 是否公司管理员
     */
    private boolean isCompanyAdmin;

    private String businessNo;

    private String companySimpleName;

    public UserModel toUserModel() {
        return UserModel.newBuilder(userId, phone).companyId(companyId).isAdmin(isCompanyAdmin).
                wechatOpenId(wechatOpenId).userName(userName).avatar(avatar).builder();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(int companyKey) {
        this.companyKey = companyKey;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isCompanyAdmin() {
        return isCompanyAdmin;
    }

    public void setCompanyAdmin(boolean companyAdmin) {
        isCompanyAdmin = companyAdmin;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getCompanySimpleName() {
        return companySimpleName;
    }

    public void setCompanySimpleName(String companySimpleName) {
        this.companySimpleName = companySimpleName;
    }
}
