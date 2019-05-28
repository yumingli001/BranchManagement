package com.yunlsp.saas.service.api.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @Author: 许路路
 * @Date: 2019/3/7
 */
public class CompanyInfoModel implements Serializable {
    @NotBlank(message = "公司id不能为空")
    private String companyId;
    @NotBlank(message = "公司名称不能为空")
    private String companyName;
    private String companySimpleName;
    private String companyAddress;
    @NotBlank(message = "统一社会信用代码不能为空")
    private String bussNo;

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

    public String getCompanySimpleName() {
        return companySimpleName;
    }

    public void setCompanySimpleName(String companySimpleName) {
        this.companySimpleName = companySimpleName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getBussNo() {
        return bussNo;
    }

    public void setBussNo(String bussNo) {
        this.bussNo = bussNo;
    }
}
