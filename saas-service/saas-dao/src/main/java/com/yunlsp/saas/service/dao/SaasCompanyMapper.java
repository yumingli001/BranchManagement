package com.yunlsp.saas.service.dao;

import com.yunlsp.saas.service.bean.SaasCompany;

/**
 * @author 许路路
 */
public interface SaasCompanyMapper {

    SaasCompany selectByCompanyId(String companyId);

    int insert(SaasCompany record);

    SaasCompany selectById(Long id);

    int update(SaasCompany record);
}