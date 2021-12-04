package com.dzd.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzd.common.utils.PageUtils;
import com.dzd.common.utils.Query;
import com.dzd.product.dao.AttrAttrgroupRelationDao;
import com.dzd.product.dao.AttrGroupDao;
import com.dzd.product.entity.AttrEntity;
import com.dzd.product.entity.AttrGroupEntity;
import com.dzd.product.service.AttrGroupService;
import com.dzd.product.service.AttrService;
import com.dzd.product.vo.AttrGroupWithAttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Resource
   AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, long catalogId) {
        if(catalogId==0){
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params), new QueryWrapper<>());
            return new PageUtils(page);
        }else{
            String key  = (String)params.get("key");
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catalogId);
            if(!StringUtils.isEmpty(key)){
                wrapper.and((obj)->{
                    obj.eq("attr_group_id",key).or().like("attr_group_name",key);
                });
            }
            return new PageUtils(this.page(new Query<AttrGroupEntity>().getPage(params),wrapper));

        }
    }

    //根据三级分类查出所有的分组以及这些组里的属性
    @Override
    public List<AttrGroupWithAttrVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        //查询分组
        List<AttrGroupEntity> entities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        //查询分组的属性
        return entities.stream().map(item->{
            AttrGroupWithAttrVo attrGroupWithAttrVo = new AttrGroupWithAttrVo();
            BeanUtils.copyProperties(item,attrGroupWithAttrVo);
            List<AttrEntity> relationAttr = attrService.getRelationAttr(attrGroupWithAttrVo.getAttrGroupId());
            attrGroupWithAttrVo.setAttrs(relationAttr);
            return attrGroupWithAttrVo;
        }).collect(Collectors.toList());
    }


}
