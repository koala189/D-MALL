package com.dzd.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzd.common.utils.PageUtils;
import com.dzd.coupon.entity.HomeSubjectSpuEntity;

import java.util.Map;

/**
 * δΈι’εε
 *
 * @author Deng ZhengDong
 * @email 22028165@zju.edu.cn
 * @date 2021-11-02 21:12:16
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

