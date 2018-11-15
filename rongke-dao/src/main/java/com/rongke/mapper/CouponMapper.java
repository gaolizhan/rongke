package com.rongke.mapper;

import com.rongke.model.Coupon;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @version : Ver 1.0
 * @CouponMapper
 * @优惠券,红包Mapper
 */
public interface CouponMapper extends BaseMapper<Coupon> {

    Integer updateAllCount();

}
