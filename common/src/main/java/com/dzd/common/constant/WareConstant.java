package com.dzd.common.constant;

import lombok.Getter;

/**
 * @author dzd
 * @Data 2021/11/29
 */
public class WareConstant {

    @Getter
    public enum PurchaseStatusEnum {
        CREATE(0, "新建"), ASSIGNED(1, "已分配"),
        RECEIVE(2, "已领取"), FINISH(3, "已完成"),
        HASERROR(4, "有异常");
        private final int code;
        private final String msg;

        PurchaseStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    @Getter
    public enum PurchaseDetailStatusEnum {
        CREATE(0, "新建"), ASSIGNED(1, "已分配"),
        BUYING(2, "正在采购"), FINISH(3, "已完成"),
        HASERROR(4, "采购失败");
        private final int code;
        private final String msg;

        PurchaseDetailStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
