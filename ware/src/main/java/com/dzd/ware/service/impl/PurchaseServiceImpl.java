package com.dzd.ware.service.impl;

import com.dzd.common.constant.WareConstant;
import com.dzd.ware.entity.PurchaseDetailEntity;
import com.dzd.ware.service.PurchaseDetailService;
import com.dzd.ware.service.WareSkuService;
import com.dzd.ware.vo.DoneReqVo;
import com.dzd.ware.vo.MergeReqVo;
import com.dzd.ware.vo.item;
import com.sun.deploy.security.MSCryptoDSASignature;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzd.common.utils.PageUtils;
import com.dzd.common.utils.Query;

import com.dzd.ware.dao.PurchaseDao;
import com.dzd.ware.entity.PurchaseEntity;
import com.dzd.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Resource
    PurchaseDetailService detailService;
    @Resource
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase() {
        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(null), new QueryWrapper<PurchaseEntity>().eq("status", 0).or().
                eq("status", 1));
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeReqVo mergeReqVo) {
        if(mergeReqVo!=null){
            Long purchaseId = mergeReqVo.getPurchaseId();

            if(purchaseId==null){
                //??????????????????????????????
                PurchaseEntity purchaseEntity = new PurchaseEntity();
                purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATE.getCode());
                purchaseEntity.setUpdateTime(new Date());
                purchaseEntity.setCreateTime(new Date());

            }else{
                // ????????????????????????0??????1
               PurchaseEntity  purchaseEntity = baseMapper.selectById(purchaseId);
                if (purchaseEntity.getStatus() != WareConstant.PurchaseStatusEnum.CREATE.getCode() || purchaseEntity.getStatus() != WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                    log.error("????????????????????????????????????");
                    return;
                }
            }
            List<Long> items = mergeReqVo.getItems();
            List<PurchaseDetailEntity> collect = items.stream().map(i -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(i);
                purchaseDetailEntity.setPurchaseId(purchaseId);
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                return purchaseDetailEntity;
            }).collect(Collectors.toList());
            detailService.saveBatch(collect);
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setId(purchaseId);
            purchaseEntity.setUpdateTime(new Date());
            this.baseMapper.updateById(purchaseEntity);
        }
    }

    @Override
    public void receivePurchase(List<Long> ids) {
        // ????????????????????????????????????
        List<PurchaseEntity> purchaseEntityList = ids.stream().map(id -> baseMapper.selectById(id)).filter(item -> (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATE.getCode() || item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode())).map(item -> {
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            return item;
        }).collect(Collectors.toList());
        // ??????
        this.updateBatchById(purchaseEntityList);
        // ?????????????????????
        purchaseEntityList.forEach(item -> {
            List<PurchaseDetailEntity> purchaseDetailEntities = detailService.getBypurchaseId(item.getId());
            List<PurchaseDetailEntity> detailEntityList = purchaseDetailEntities.stream().map(entity -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(entity.getId());
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return purchaseDetailEntity;
            }).collect(Collectors.toList());
            detailService.updateBatchById(detailEntityList);
        });
    }

    @Override
    public void done(DoneReqVo doneReqVo) {
        // 1???????????????????????????
        List<item> items = doneReqVo.getItems();
        Boolean flag = true;
        List<PurchaseDetailEntity> updates = new ArrayList<>(10);
        for (item item : items) {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                // ????????????
                log.error("????????????????????????????????????");
                flag = false;
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode());
            } else {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                // 3???????????????????????????
                PurchaseDetailEntity detailEntity = detailService.getById(item.getItemId());
                wareSkuService.addStock(detailEntity.getSkuId(), detailEntity.getWareId(), detailEntity.getSkuNum());
            }
            purchaseDetailEntity.setId(item.getItemId());
            updates.add(purchaseDetailEntity);
        }
        detailService.updateBatchById(updates);
        // 2?????????????????????
        PurchaseEntity purchaseEntity = baseMapper.selectById(doneReqVo.getId());
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setId(doneReqVo.getId());
        baseMapper.updateById(purchaseEntity);
    }

}
