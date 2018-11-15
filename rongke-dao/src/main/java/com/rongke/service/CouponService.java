package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.Coupon;

/**
 * @version : Ver 1.0
 * @CouponService
 * @优惠券,红包Service
 */
public interface CouponService extends IService<Coupon> {
    Integer updateAllCount();
}
