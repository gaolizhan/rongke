package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.UserCoupon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserCouponMapper
 * @用户优惠券列Mapper
 */
public interface UserCouponMapper extends BaseMapper<UserCoupon> {
    //邀请奖励统计
    Integer selectFriendCount(Long id);

    BigDecimal selectFriendMoney(Long id);
    //优惠券列表
    List<Map> selectUserCouponList(Long id);

    List<UserCoupon> findByUserPage(Map<String,Object> map);

    Integer selectByUserCount(Map<String,Object> map);

    //所有过期的优惠券根据user分组
    List<UserCoupon>selectUserCouponPast();

}
