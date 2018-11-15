package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.CouponMapper;
import com.rongke.model.Coupon;
import com.rongke.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version : Ver 1.0
 * @CouponServiceImpl
 * @优惠券,红包ServiceImpl
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    public Integer updateAllCount(){
        return couponMapper.updateAllCount();
    }
}
