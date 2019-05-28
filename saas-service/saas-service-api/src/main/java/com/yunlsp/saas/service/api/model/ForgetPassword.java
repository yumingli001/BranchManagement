package com.yunlsp.saas.service.api.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 忘记密码
 *
 * @author 许路路
 */
public class ForgetPassword implements Serializable {

    @NotBlank(message = "用户手机号码必须")
    private String phone;

    @NotBlank(message = "短信验证码不能为空")
    private String messageCode;

    @Length(min = 6, max = 20, message = "密码要求最短6位,最多20位")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    private String confirmPassword;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
