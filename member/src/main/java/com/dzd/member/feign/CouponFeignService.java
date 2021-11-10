package com.dzd.member.feign;

import com.dzd.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author:DengZD
 * @description:
 * @date:2021-11-03
 * @modified By:
 */
@FeignClient("coupon")
public interface CouponFeignService {
    @RequestMapping("/coupon/coupon/member/list")
    public R memberCoupons();
}
