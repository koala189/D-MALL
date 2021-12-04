package com.dzd.ware.feign;

import com.dzd.common.to.SkuInfoEntity;
import com.dzd.common.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author:DengZD
 * @description:
 * @date:2021-12-03
 * @modified By:
 */
@FeignClient("gateway")
public interface ProductFeignService {
    /**
     * 信息
     */
    @RequestMapping("/api/product/skuinfo/getSkuName")
    //@RequiresPermissions("product:skuinfo:info")
    public String getSkuName(@RequestParam Long skuId);




}
