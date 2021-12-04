package com.dzd.product.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dzd.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author:DengZD
 * @description:
 * @date:2021-11-24
 * @modified By:
 */
@Data
public class AttrGroupWithAttrVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
