package com.yunlsp.saas.service.api;

import com.yunlsp.common.domain.Result;
import com.yunlsp.common.exception.ParamException;

/**
 * @Author: 许路路
 * @Date: 2019/3/5
 */
public interface UserLoginService {

    String sayHi(String name);

    Result loginCheck(String account, String password);

    void resetPassword(String phone, String newPassword) throws ParamException;

}
