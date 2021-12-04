package com.dzd.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzd.common.utils.PageUtils;
import com.dzd.product.entity.BrandEntity;
import com.dzd.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * @author:DengZD
 * @description:
 * @date:2021-11-13
 * @modified By:
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveIdAndName(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrand(Long brandId, String name);

    void updateCategory(Long catId, String name);

    List<BrandEntity> getBrandByCatlogId(Long catId);

    List<BrandEntity> getBrandsById(Long catId);
}
