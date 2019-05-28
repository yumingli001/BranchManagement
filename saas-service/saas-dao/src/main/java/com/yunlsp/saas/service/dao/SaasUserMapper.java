package com.yunlsp.saas.service.dao;

import com.yunlsp.saas.service.api.model.UserDetailModel;
import com.yunlsp.saas.service.bean.SaasUser;

/**
 * @author 许路路
 */
public interface SaasUserMapper {

    int insert(SaasUser record);

    SaasUser selectById(Integer id);

    int update(SaasUser record);

    SaasUser selectByPhone(String phone);

    SaasUser selectByUserId(String userId);

    UserDetailModel selectUserInfoByUserId(String userId);
}