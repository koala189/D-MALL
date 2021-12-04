package com.dzd.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzd.common.constant.ProductConstant;
import com.dzd.common.utils.PageUtils;
import com.dzd.common.utils.Query;
import com.dzd.product.dao.*;
import com.dzd.product.entity.AttrAttrgroupRelationEntity;
import com.dzd.product.entity.AttrEntity;
import com.dzd.product.entity.AttrGroupEntity;
import com.dzd.product.entity.CategoryEntity;
import com.dzd.product.service.AttrService;
import com.dzd.product.service.ProductAttrValueService;
import com.dzd.product.vo.AttrGroupAndAttrVo;
import com.dzd.product.vo.AttrResVo;
import com.dzd.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Resource
    AttrGroupDao attrGroupDao;
    @Resource
    CategoryDao categoryDao;
    @Resource
    AttrAttrgroupRelationDao RelationDao;
    @Resource
    private CategoryServiceImpl categoryService;

    @Resource
    private ProductAttrValueService productAttrValueService;

    @Resource
    private ProductAttrValueDao productAttrValueDao;


    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("attr_type","base".equalsIgnoreCase(type)?1:0);
        if(catelogId!=0){
            wrapper.eq("catelog_id",catelogId);
        }
        String key =(String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and(queryWrapper-> {
                queryWrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrResVo> respVos = records.stream().map(attrEntity -> {
            AttrResVo attrResVo = new AttrResVo();
            BeanUtils.copyProperties(attrEntity, attrResVo);

            //设置分类和分组的名字
            AttrAttrgroupRelationEntity attr_id = RelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().
                    eq("attr_id", attrEntity.getAttrId()));

            if (attr_id != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attr_id.getAttrGroupId());
                if (attrGroupEntity != null)
                    attrResVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrResVo.setCatelogName(categoryEntity.getName());
            }
            return attrResVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrResVo getAttrInfo(Long attrId) {
        AttrResVo attrResVo = new AttrResVo();
        AttrEntity attrEntity = this.getById(attrId);
        Long catelogId = attrEntity.getCatelogId();
        BeanUtils.copyProperties(attrEntity,attrResVo);
        if(attrEntity.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //设置分组信息
            AttrAttrgroupRelationEntity attrgroup_relation = RelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().
                    eq("attr_id", attrId));
            if(attrgroup_relation!=null){
                attrResVo.setAttrGroupId(attrgroup_relation.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroup_relation.getAttrGroupId());
                if(attrGroupEntity!=null){
                    attrResVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }
        //设置分类信息
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        attrResVo.setCatelogPath(catelogPath);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        attrResVo.setCatelogName(categoryEntity.getName());
        return attrResVo;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<>()
        );
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveVo(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        baseMapper.insert(attrEntity);
        // 保存分组关系
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity groupEntity = new AttrAttrgroupRelationEntity();
            groupEntity.setAttrGroupId(attr.getAttrGroupId());
            groupEntity.setAttrId(attrEntity.getAttrId());
            RelationDao.insert(groupEntity);
        }
    }

    @Transactional
    @Override
    public void updateVo(AttrVo attr) {
        // 修改基本属性
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        baseMapper.updateById(attrEntity);
        // 修改分组关联
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            Integer count = RelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attr.getAttrId()));
            if (count > 0) {
                RelationDao.update(relationEntity, new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attrEntity.getAttrId()));
            } else {
                relationEntity.setAttrId(attr.getAttrId());
                RelationDao.insert(relationEntity);
            }
        }
    }

    //在 关联表中根据Attrgroupid查找所有的attrId，再根据attrID去attr表里查询关联的所有属性
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entities= RelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().
                eq("attr_group_id", attrgroupId));
        List<Long> attrIds = entities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        return (List<AttrEntity>) this.listByIds(attrIds);
    }



    //删除属性与分组的关联关系
    @Override
    public void deleteRelation(AttrGroupAndAttrVo[] attrVos) {

        //批量删除
        List<AttrAttrgroupRelationEntity> entities= Arrays.stream(attrVos).map(item -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        RelationDao.deleteBatchRelation(entities);
    }

    //获取当前分组的没有关联的所有属性
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //当前分组只能关联自己所属分类里的属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        PageUtils pageUtils = null;
        if(attrGroupEntity!=null){
            Long catelogId = attrGroupEntity.getCatelogId();

            //当前分组只能关联别的分组没有引用的属性 :
            // 首先找到当前分类下的其他分组，再找到这些分组关联的属性，从当前分类的所有属性中移除这些属性

            List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().
                    eq("catelog_id", catelogId));
            List<Long> groupIdList = attrGroupEntities.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
            QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
            if(groupIdList.size() != 0){
                wrapper.in("attr_group_id",groupIdList);
            }
            // 查询这些分组关联的属性
            List<AttrAttrgroupRelationEntity> relationEntityList = RelationDao.selectList(wrapper);
            List<Long> attrIdList = relationEntityList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
            // 获取未被关联的属性的条件
            QueryWrapper<AttrEntity> wrapper2 = new QueryWrapper<AttrEntity>()
                    .eq("catelog_id", attrGroupEntity.getCatelogId());
            if (attrIdList.size() > 0) {
                wrapper2.notIn("attr_id", attrIdList);
            }
            String key = (String) params.get("key");
            if (!StringUtils.isEmpty(key)) {
                wrapper2.and((w) -> {
                    w.eq("attr_id", key).or().like("attr_name", key);
                });
            }
            IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper2);
            pageUtils = new PageUtils(page);
        }
        return pageUtils;
    }

}

