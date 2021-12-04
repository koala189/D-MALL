package com.dzd.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzd.common.utils.PageUtils;
import com.dzd.common.utils.Query;
import com.dzd.product.dao.BrandDao;
import com.dzd.product.dao.CategoryBrandRelationDao;
import com.dzd.product.dao.CategoryDao;
import com.dzd.product.entity.BrandEntity;
import com.dzd.product.entity.CategoryBrandRelationEntity;
import com.dzd.product.entity.CategoryEntity;
import com.dzd.product.service.BrandService;
import com.dzd.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:DengZD
 * @description:
 * @date:2021-11-13
 * @modified By:
 */
@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl  extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    BrandDao brandDao;
    @Autowired
    CategoryDao categoryDao;
    @Resource
    CategoryBrandRelationDao relationDao;
    @Resource
    BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveIdAndName(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

    }

    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);
        categoryBrandRelationEntity.setBrandName(name);
        this.update(categoryBrandRelationEntity,
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));

    }

    @Override
    public void updateCategory(Long catId, String name) {
        this.baseMapper.updateCategory(catId,name);
    }

    @Override
    public List<BrandEntity> getBrandByCatlogId(Long catId) {
        List<CategoryBrandRelationEntity> entity = baseMapper.selectList(new QueryWrapper<CategoryBrandRelationEntity>().
                eq("catelog_id", catId));

        List<Long> brandIds = entity.stream().
                map(CategoryBrandRelationEntity::getBrandId).collect(Collectors.toList());
        return  brandDao.selectBatchIds(brandIds);
    }

    @Override
    public List<BrandEntity> getBrandsById(Long catId) {
        List<CategoryBrandRelationEntity> entities = relationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        return  entities.stream().map(item -> {
            Long brandId = item.getBrandId();
            return brandService.getById(brandId);
        }).collect(Collectors.toList());
    }


}
