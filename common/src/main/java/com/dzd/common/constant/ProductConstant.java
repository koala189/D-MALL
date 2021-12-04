package com.dzd.common.constant;

/**
 * @author:DengZD
 * @description:
 * @date:2021-11-22
 * @modified By:
 */
public class ProductConstant {
    public  enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性"),
        DEFAULT_IMG(1, "默认图片"), NOT_DEFAULT_IMG(0, "非默认图片"),
        SUCCESS_FEIGN(0, "远程服务调用成功"), ERROR_FEIGN(1, "远程服务调用失败");

        AttrEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }
        private final int code;
        private final String msg;

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
