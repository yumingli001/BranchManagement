package com.yunlsp.saas.service.api.portal.enums;

/**
 * @Author: 许路路
 * @Date: 2019/3/7
 */
public enum ErrorCodeEnum {

    /**
     * 未登录
     */
    NO_LOGIN(402, "未登录"),

    NO_AUTHORITY(403, "没有权限");

    private int code;

    private String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ErrorCodeEnum match(int code) {
        ErrorCodeEnum[] values = ErrorCodeEnum.values();
        for (ErrorCodeEnum value : values) {
            if (code == value.getCode()) {
                return value;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
