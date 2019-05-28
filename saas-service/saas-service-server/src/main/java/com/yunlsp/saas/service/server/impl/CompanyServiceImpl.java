package com.yunlsp.saas.service.server.impl;

import com.yunlsp.common.exception.ParamException;
import com.yunlsp.common.validate.HibernateParamValidateUtils;
import com.yunlsp.saas.service.api.CompanyService;
import com.yunlsp.saas.service.api.model.CompanyInfoModel;
import com.yunlsp.saas.service.bean.SaasCompany;
import com.yunlsp.saas.service.dao.SaasCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: 许路路
 * @Date: 2019/3/7
 */
@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private SaasCompanyMapper companyMapper;

    @Override
    public CompanyInfoModel companyInfo(String companyId) {
        CompanyInfoModel model = new CompanyInfoModel();
        SaasCompany company = companyMapper.selectByCompanyId(companyId);
        if (company != null) {
            model.setBussNo(company.getBusinessNo());
            model.setCompanyAddress(company.getCompanyAddress());
            model.setCompanyId(company.getCompanyId());
            model.setCompanyName(company.getCompanyName());
            model.setCompanySimpleName(company.getCompanySimpleName());
        }
        return model;
    }

    @Override
    public void modifyCompanyInfo(CompanyInfoModel companyInfo) throws ParamException {
        HibernateParamValidateUtils.validate(companyInfo);
        SaasCompany company = companyMapper.selectByCompanyId(companyInfo.getCompanyId());
        if (company == null) {
            throw new ParamException("公司不存在：" + companyInfo.getCompanyId());
        }
        company.setModifyTime(new Date());
        company.setCompanySimpleName(companyInfo.getCompanySimpleName());
        company.setCompanyAddress(companyInfo.getCompanyAddress());
        companyMapper.update(company);
    }
}
