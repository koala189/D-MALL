package com.dzd.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author dzd
 * @Data 2021/11/29
 */
@Data
public class DoneReqVo {

    /**
     * 采购单id
     */
    @NotNull
    private Long id;
    /**
     * //完成/失败的需求详情
     */
    private List<com.dzd.ware.vo.item> items;
}
