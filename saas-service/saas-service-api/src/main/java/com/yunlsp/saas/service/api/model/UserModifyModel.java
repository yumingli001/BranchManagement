package com.yunlsp.saas.service.api.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 用户基本信息修改
 *
 * @author 许路路
 */
public class UserModifyModel implements Serializable {

    @NotBlank(message = "userId 不能为空")
    private String userId;
    @NotBlank(message = "姓名必须不能为空")
    private String name;
    private String image;
    private String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
