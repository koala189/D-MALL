package com.dzd.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzd.common.to.SkuReductionTo;
import com.dzd.common.utils.PageUtils;
import com.dzd.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author Deng ZhengDong
 * @email 22028165@zju.edu.cn
 * @date 2021-11-02 21:12:16
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

