package com.dzd.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzd.common.utils.PageUtils;
import com.dzd.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Deng ZhengDong
 * @email 22028165@zju.edu.cn
 * @date 2021-11-02 21:32:57
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseDetailEntity> getBypurchaseId(Long id);
}

