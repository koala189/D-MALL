package com.dzd.product.vo;


import com.dzd.product.entity.AttrEntity;
import com.dzd.product.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author hl
 * @Data 2020/11/27
 */
@Data
public class AttrGroupAndAttrVo extends AttrGroupEntity {

    /**
     * 属性列表
     */
    private List<AttrEntity> attrs;
}
