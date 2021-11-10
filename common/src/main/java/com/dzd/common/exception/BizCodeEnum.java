package com.dzd.common.exception;

/**
 * @author:DengZD
 * @description:
 * @date:2021-11-10
 * @modified By:
 */
public enum BizCodeEnum {
    UNKONW_EXCEPTON(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败");

    Integer code;
    String msg;

    BizCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
