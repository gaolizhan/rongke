package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserCouponMapper;
import com.rongke.model.UserCoupon;
import com.rongke.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserCouponServiceImpl
 * @用户优惠券列ServiceImpl
 */
@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {
    @Autowired
    private UserCouponMapper UserCouponDao;

    public Integer selectFriendCount(Long id) {
        return (UserCouponDao.selectFriendCount(id));
    }

    public BigDecimal selectFriendMoney(Long id) {
        return (UserCouponDao.selectFriendMoney(id));
    }

    //优惠券列表
    public List<Map> selectUserCouponList(Long id){
        return (UserCouponDao.selectUserCouponList(id));
    }

    public List<UserCoupon> findByUserPage( Map<String,Object> map){return (UserCouponDao.findByUserPage(map));};

    public Integer selectByUserCount(Map<String,Object> map){return (UserCouponDao.selectByUserCount(map));};

    //所有过期的优惠券根据user分组
    public List<UserCoupon>selectUserCouponPast(){
        return UserCouponDao.selectUserCouponPast();
    };
}
