package com.yunlsp.saas.service.api;

import com.yunlsp.common.exception.ParamException;
import com.yunlsp.saas.service.api.model.CompanyInfoModel;

/**
 * @Author: 许路路
 * @Date: 2019/3/7
 */
public interface CompanyService {

    CompanyInfoModel companyInfo(String companyId);

    void modifyCompanyInfo(CompanyInfoModel companyInfo) throws ParamException;

}
