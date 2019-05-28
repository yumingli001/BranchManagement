package com.yunlsp.saas.service.server.dao;

import com.yunlsp.saas.service.dao.SaasCompanyMapper;
import com.yunlsp.saas.service.bean.SaasCompany;
import com.yunlsp.saas.service.server.SaasServerStartup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * @Author: 许路路
 * @Date: 2019/3/6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SaasServerStartup.class)
public class SaasCompanyMapperTest {

    @Autowired
    private SaasCompanyMapper companyMapper;

    @Test
    public void insetTest() {
        SaasCompany company = new SaasCompany();
        company.setAuditResult(2);
        company.setCompanyId("5555555555" + new Date());
        company.setBusinessNo("23333333333");
        company.setCompanyAddress("rrrrrrrrrrrr");
        company.setCompanyEmail("e454554");
        company.setCompanyName("ddddd");
        company.setCreateBy("errrrr");
        company.setContactsInfo("dddddddddd");
        int res = companyMapper.insert(company);
        Assert.assertTrue(res == 1);
    }

}