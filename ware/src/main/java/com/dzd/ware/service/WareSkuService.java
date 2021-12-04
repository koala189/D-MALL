package com.dzd.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzd.common.utils.PageUtils;
import com.dzd.ware.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 商品库存
 *
 * @author Deng ZhengDong
 * @email 22028165@zju.edu.cn
 * @date 2021-11-02 21:32:57
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    //多个参数一定要为每个参数指定param
}

