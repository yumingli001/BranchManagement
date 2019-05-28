package com.yunlsp.saas.service.server.dao;

import com.yunlsp.common.encryption.Md5Utils;
import com.yunlsp.saas.service.api.model.UserDetailModel;
import com.yunlsp.saas.service.dao.SaasUserMapper;
import com.yunlsp.saas.service.bean.SaasUser;
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
public class SaasUserMapperTest {

    @Autowired
    private SaasUserMapper userMapper;

    @Test
    public void insetTest() {
        SaasUser user = new SaasUser();
        user.setUserId("su" + System.currentTimeMillis());
        user.setUserName("test");
        user.setAccountExpiryDate(new Date());
        user.setPassword(Md5Utils.getMD5(Md5Utils.getMD5("12345678" + "123456" + "12345678")));
        user.setPswSalt("12345678");
        user.setEmail("1232323");
        user.setPhone("18352410952");
        user.setStatus((byte) 1);
        int res = userMapper.insert(user);
        Assert.assertTrue(res == 1);
    }

    @Test
    public void selectUserInfoByUserId() {
        String userId = "su1551860558292";
        UserDetailModel detailModel = userMapper.selectUserInfoByUserId(userId);
        if (detailModel != null) {
            Assert.assertTrue(detailModel.getUserId().equals(userId));
        }
    }

    @Test
    public void update() {
        String userId = "su1551860558292";
        SaasUser user = userMapper.selectByUserId(userId);
        if (user != null) {
            user.setRemark("备注修改");
            userMapper.update(user);
        }
    }

}
