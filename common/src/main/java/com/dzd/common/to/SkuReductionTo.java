package com.dzd.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author dzd
 * @Data 2021/11/28
 */
//sku折扣
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;

}
