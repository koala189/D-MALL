package com.dzd.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author dzd
 * @Data 2020/11/29
 */
@Data
public class MergeReqVo {
    /**
     * 整单id
     */
    private Long purchaseId;
    /**
     * 合并项集合
     */
    private List<Long> items;
}
