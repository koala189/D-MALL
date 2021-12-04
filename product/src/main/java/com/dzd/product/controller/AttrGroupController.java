package com.dzd.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.dzd.product.entity.AttrEntity;
import com.dzd.product.service.AttrAttrgroupRelationService;
import com.dzd.product.service.AttrService;
import com.dzd.product.service.CategoryService;
import com.dzd.product.vo.AttrGroupAndAttrVo;
import com.dzd.product.vo.AttrGroupRelationVo;
import com.dzd.product.vo.AttrGroupWithAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dzd.product.entity.AttrGroupEntity;
import com.dzd.product.service.AttrGroupService;
import com.dzd.common.utils.PageUtils;
import com.dzd.common.utils.R;

import javax.annotation.Resource;


/**
 * 属性分组
 *
 * @author Deng ZhengDong
 * @email 22028165@zju.edu.cn
 * @date 2021-11-02 21:37:42
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Resource
    private AttrService attrService;
    @Resource
    AttrAttrgroupRelationService relationService;

    // /product/attrgroup/{catelogId}/withattr
    //查出当前分类下的所有属性分组，并查出每个属性分组的所有属性
    @RequestMapping("/{catelogId}/withattr")
    public R getAttrGroup(@PathVariable("catelogId") Long catelogId){
       List<AttrGroupWithAttrVo> vos =attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
       return R.ok().put("data",vos);
    }
    // /product/attrgroup/attr/relation
    @RequestMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
        relationService.saveBatch(vos);
        return R.ok();
    }
    // /product/attrgroup/{attrgroupId}/attr/relation
    @RequestMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long  attrgroupId){
       List<AttrEntity> attrEntityList =  attrService.getRelationAttr(attrgroupId);
       return R.ok().put("data",attrEntityList);
    }

    @RequestMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@RequestParam Map<String, Object> params,
                            @PathVariable("attrgroupId") Long  attrgroupId){
       PageUtils page=  attrService.getNoRelationAttr(params,attrgroupId);
       return R.ok().put("page",page);

    }


        /**
         * 列表
         */
    @RequestMapping("/list/{catalogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catalogId") long catalogId ){
        PageUtils page = attrGroupService.queryPage(params,catalogId);

        return R.ok().put("page", page);
    }

   //  /product/attrgroup/attr/relation/delete post请求  请求参数 :[{"attrId":1,"attrGroupId":2}]


    @RequestMapping("/attr/relation/delete")
    public R deleteRelation(AttrGroupAndAttrVo[] attrVos){
        attrService.deleteRelation(attrVos);
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
