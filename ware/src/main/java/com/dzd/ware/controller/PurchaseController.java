package com.dzd.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.dzd.ware.vo.DoneReqVo;
import com.dzd.ware.vo.MergeReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dzd.ware.entity.PurchaseEntity;
import com.dzd.ware.service.PurchaseService;
import com.dzd.common.utils.PageUtils;
import com.dzd.common.utils.R;



/**
 * 采购信息
 *
 * @author Deng ZhengDong
 * @email 22028165@zju.edu.cn
 * @date 2021-11-02 21:32:57
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;


    /**
     * 完成采购
     */
    @PostMapping("/done")

    public R done(@RequestBody DoneReqVo doneReqVo) {
        purchaseService.done(doneReqVo);
        return R.ok();
    }

    /**
     * 领取采购单
     */
    @PostMapping("/received")

    public R receivePurchase(@RequestParam List<Long> ids) {
        purchaseService.receivePurchase(ids);
        return R.ok();
    }

    /**
     * 合并采购单
     */
    //RequestBody的作用是将请求体中的json数据转化为相应的对象
    @PostMapping("/merge")
    public R merge(@RequestBody MergeReqVo mergeReqVo){
        purchaseService.mergePurchase(mergeReqVo);
        return R.ok();
    }

    /**
     * 查询分配和新建的采购单
     */
    @RequestMapping("unreceive/list")
    //@RequiresPermissions("ware:purchase:list")
    public R unreceiveList(){
        PageUtils page = purchaseService.queryPageUnreceivePurchase();

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
		purchaseService.save(purchase);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
