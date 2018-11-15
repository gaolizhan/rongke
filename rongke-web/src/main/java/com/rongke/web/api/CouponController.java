package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.FailException;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.Coupon;
import com.rongke.model.User;
import com.rongke.model.UserCoupon;
import com.rongke.service.CouponService;
import com.rongke.service.UserCouponService;
import com.rongke.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @version : Ver 1.0
 * @CouponController
 * @优惠券,红包Controller
 */
@RestController
@RequestMapping(value = "/api/coupon")
@Transactional
@CrossOrigin
public class CouponController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private CouponService couponService;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserService userService;

    /**
     * @param coupon
     * @return 返回值JsonResp
     * @添加优惠券,红包
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addCoupon(@RequestBody Coupon coupon) {
        log.debug("添加优惠券,红包");
        couponService.insert(coupon);
        return JsonResp.ok(coupon);
    }

    /**
     * @param coupon
     * @return 返回值JsonResp
     * @修改优惠券,红包
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateCoupon(@RequestBody Coupon coupon) {
        log.debug("修改优惠券,红包");
        couponService.updateById(coupon);
        return JsonResp.ok(coupon);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找优惠券,红包
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectCoupon(Long id) {
        log.debug("查找优惠券,红包");
        Coupon coupon = couponService.selectById(id);
        return JsonResp.ok(coupon);
    }


    /**
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value = "/findByPage", method = RequestMethod.GET)
    public JsonResp findByPage(Integer pageNo, Integer pageSize,String realName,Integer status,Integer type){
        log.debug("分页查询优惠券列表");
        Page page = new Page(pageNo,pageSize);
        EntityWrapper<Coupon> entityWrapper = new EntityWrapper<>();
        if (!realName.equals("")) {
            entityWrapper.like("coupou_name",realName);
        }
        if (status != -1) {
            entityWrapper.eq("coupon_status",status);
        }
        if (type != -1) {
            entityWrapper.eq("type",type);
        }
        Page page1 = couponService.selectPage(page,entityWrapper);
        PageDto pageDto = new PageDto(pageNo,pageSize,page1.getRecords(),page1.getTotal());
        return JsonResp.ok(pageDto);
    }


    /**
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.GET)
    public JsonResp updateStatus(Long id,Integer status,Integer type){
        log.debug("更改优惠券状态");
        if (status == 1) {
            EntityWrapper<Coupon> entityWrapper = new EntityWrapper();
            entityWrapper.eq("coupon_status",1);
            entityWrapper.eq("type",type);
            List<Coupon> list = couponService.selectList(entityWrapper);
            if (list.size() == 1) {
                throw new FailException("只能启用一种");
            }
        }

        Coupon coupon = couponService.selectById(id);
        coupon.setCouponStatus(status);
        couponService.updateById(coupon);
        return JsonResp.ok();
    }

    /**
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value = "/giveOut", method = RequestMethod.POST)
    public JsonResp giveOut(@RequestBody List<Long> userIds,Long id,boolean allGiveOut) throws Exception{
        log.debug("发放优惠券");
        Coupon c1 = couponService.selectById(id);
        Integer dayTime = c1.getValidTime();
        //全员发放
        if (allGiveOut == true) {
            couponService.updateAllCount();
            EntityWrapper<User> entityWrapper = new EntityWrapper();
            List<UserCoupon> list = new ArrayList<>();
            entityWrapper.eq("status",1);
            entityWrapper.eq("auth_status",1);
            List<User> list1 = userService.selectList(entityWrapper);
            Date str;
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Calendar lastDate = Calendar.getInstance();
            lastDate.add(Calendar.DATE, dayTime);//日期加7天
            str=sdf.parse(sdf.format(lastDate.getTime()));
            for (User user : list1) {
                UserCoupon u = new UserCoupon();
                u.setUserId(user.getId());
                u.setCouponId(id);
                u.setPastDatetime(str);
                u.setSaveMoney(c1.getSaveMoney());
                list.add(u);
            }
            userCouponService.insertBatch(list);
        }else {//部分发放
            Date str;
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Calendar lastDate = Calendar.getInstance();
            lastDate.add(Calendar.DATE, dayTime);//日期加7天
            str=sdf.parse(sdf.format(lastDate.getTime()));
            List<UserCoupon> list = new ArrayList<>();
            for (Long userId : userIds) {
                UserCoupon u = new UserCoupon();
                u.setUserId(userId);
                u.setCouponId(id);
                u.setPastDatetime(str);
                u.setSaveMoney(c1.getSaveMoney());
                list.add(u);
            }
            userCouponService.insertBatch(list);
        }
        return JsonResp.ok();
    }



}
