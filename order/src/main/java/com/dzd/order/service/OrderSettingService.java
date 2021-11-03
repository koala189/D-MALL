package com.dzd.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzd.common.utils.PageUtils;
import com.dzd.order.entity.OrderSettingEntity;

import java.util.Map;

/**
 * 订单配置信息
 *
 * @author Deng ZhengDong
 * @email 22028165@zju.edu.cn
 * @date 2021-11-02 21:29:25
 */
public interface OrderSettingService extends IService<OrderSettingEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

