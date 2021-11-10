package com.dzd.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzd.common.utils.PageUtils;
import com.dzd.common.utils.Query;

import com.dzd.product.dao.CategoryDao;
import com.dzd.product.entity.CategoryEntity;
import com.dzd.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        /*List<CategoryEntity> entities = baseMapper.selectList(null);
        return  entities.stream().filter(categoryEntity -> categoryEntity.getParentCid()
                        == 0).map((menu)->{
                            //设置子菜单
                            menu.setChildren(getChildrens(menu,entities));
                            return menu;
                }).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))).
                collect(Collectors.toList());*/
        return getChildrens(null,baseMapper.selectList(null));
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 检查当前删除的菜单，是否被其他地方引用

        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    /**
     * @paramter: root：父分类，all：所有分类
     * @return: 所有子分类
     * @Description: 通过parentid和cid，递归的将所有所有父分类的子分类赋给父分类的children属性
     * @author: DengZD
     * @createDate 2021/11/9
     * @update:
     */
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){
        return  all.stream().filter(root == null? (categoryEntity -> categoryEntity.getParentCid()==0):
                ( categoryEntity -> categoryEntity.getParentCid() == root.getCatId() )).map(
                menu -> {
                    menu.setChildren(getChildrens(menu, all));
                    return menu;
                }).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))).collect(
                Collectors.toList());

    }

}