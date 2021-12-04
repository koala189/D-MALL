package com.dzd.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzd.common.to.SpuBoundsTo;
import com.dzd.common.utils.PageUtils;
import com.dzd.coupon.entity.SpuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author Deng ZhengDong
 * @email 22028165@zju.edu.cn
 * @date 2021-11-02 21:12:16
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBounds(SpuBoundsTo spuBoundsTo);
}

