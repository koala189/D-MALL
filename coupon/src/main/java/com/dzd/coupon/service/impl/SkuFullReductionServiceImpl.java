package com.dzd.coupon.service.impl;

import com.dzd.common.to.MemberPrice;
import com.dzd.common.to.SkuReductionTo;
import com.dzd.coupon.dao.SkuLadderDao;
import com.dzd.coupon.entity.MemberPriceEntity;
import com.dzd.coupon.entity.SkuLadderEntity;
import com.dzd.coupon.service.MemberPriceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzd.common.utils.PageUtils;
import com.dzd.common.utils.Query;

import com.dzd.coupon.dao.SkuFullReductionDao;
import com.dzd.coupon.entity.SkuFullReductionEntity;
import com.dzd.coupon.service.SkuFullReductionService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {
    @Resource
    MemberPriceService memberPriceService;
    @Resource
    SkuLadderDao skuLadderDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        // 保存满减价格 sms_sku_ladder
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        skuLadderDao.insert(skuLadderEntity);
        // 保存满减信息 sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        skuFullReductionEntity.setAddOther(skuReductionTo.getCountStatus());
        skuFullReductionEntity.setFullPrice(skuReductionTo.getFullPrice());
        skuFullReductionEntity.setReducePrice(skuReductionTo.getReducePrice());
        skuFullReductionEntity.setSkuId(skuReductionTo.getSkuId());
        baseMapper.insert(skuFullReductionEntity);
        // 保存会员价格 sms_member_price
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntityList = memberPrice.stream().map(item -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setAddOther(skuReductionTo.getCountStatus());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setMemberPrice(item.getPrice());
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            return memberPriceEntity;
        }).filter(item -> (item.getMemberPrice().compareTo(new BigDecimal("0")) > 0)).collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntityList);
    }

}
