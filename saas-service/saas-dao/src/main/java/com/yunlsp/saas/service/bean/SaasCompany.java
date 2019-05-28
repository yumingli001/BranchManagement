package com.yunlsp.saas.service.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 许路路
 */
public class SaasCompany implements Serializable {
    private Integer id;

    private String companyId;

    private String companyName;

    private String companyType;

    private String companyAddress;

    private String businessNo;

    private String fax;

    private String telephone;

    private String companyEmail;

    private Date createTime;

    private Date modifyTime;

    private String remark;

    private Integer auditResult;

    private String createBy;

    private String contactsInfo;

    private String companySimpleName;

    private String nature;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType == null ? null : companyType.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo == null ? null : businessNo.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail == null ? null : companyEmail.trim();
    }

    public Date getCreateTime() {
        return createTime == null ? new Date() : createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime == null ? new Date() : modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(Integer auditResult) {
        this.auditResult = auditResult;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getContactsInfo() {
        return contactsInfo;
    }

    public void setContactsInfo(String contactsInfo) {
        this.contactsInfo = contactsInfo == null ? null : contactsInfo.trim();
    }

    public String getCompanySimpleName() {
        return companySimpleName;
    }

    public void setCompanySimpleName(String companySimpleName) {
        this.companySimpleName = companySimpleName == null ? null : companySimpleName.trim();
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature == null ? null : nature.trim();
    }
}