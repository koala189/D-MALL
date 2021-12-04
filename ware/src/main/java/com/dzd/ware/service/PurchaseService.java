package com.dzd.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzd.common.utils.PageUtils;
import com.dzd.ware.entity.PurchaseEntity;
import com.dzd.ware.vo.DoneReqVo;
import com.dzd.ware.vo.MergeReqVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author Deng ZhengDong
 * @email 22028165@zju.edu.cn
 * @date 2021-11-02 21:32:57
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivePurchase();

    void mergePurchase(MergeReqVo mergeReqVo);

    void receivePurchase(List<Long> ids);

    void done(DoneReqVo doneReqVo);
}

