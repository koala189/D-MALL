package com.dzd.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzd.common.utils.PageUtils;
import com.dzd.product.entity.AttrEntity;
import com.dzd.product.vo.AttrGroupAndAttrVo;
import com.dzd.product.vo.AttrResVo;
import com.dzd.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author Deng ZhengDong
 * @email 22028165@zju.edu.cn
 * @date 2021-11-02 21:37:42
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrResVo getAttrInfo(Long attrId);

   void saveVo(AttrVo attr);

    void updateVo(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupAndAttrVo[] attrVos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);
}

