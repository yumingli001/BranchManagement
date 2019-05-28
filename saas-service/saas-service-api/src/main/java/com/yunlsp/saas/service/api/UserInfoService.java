package com.yunlsp.saas.service.api;

import com.yunlsp.common.exception.ParamException;
import com.yunlsp.saas.service.api.model.UserDetailModel;
import com.yunlsp.saas.service.api.model.UserModifyModel;

/**
 * @Author: 许路路
 * @Date: 2019/3/7
 */
public interface UserInfoService {

    void modifyUserInfo(UserModifyModel modifyModel) throws ParamException;

    void modifyPhone(String userId, String newPhone) throws ParamException;

    void modifyPassword(String userId, String oldPsw, String newPsw) throws ParamException;

    boolean phoneIsExsit(String phone);

    UserDetailModel userDetailInfo(String userId);
}
