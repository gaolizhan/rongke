package com.rongke.web.api;

import com.rongke.annotation.SourceAuthority;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.enums.SourceType;
import com.rongke.model.User;
import com.rongke.model.UserCoupon;
import com.rongke.service.UserCouponService;
import com.rongke.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserCouponController
 * @用户优惠券列Controller
 */
@RestController
@RequestMapping(value = "/api/userCoupon")
@Transactional
@CrossOrigin
public class UserCouponController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserService userService;

    /**
     * @param userCoupon
     * @return 返回值JsonResp
     * @添加用户优惠券列
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addUserCoupon(@RequestBody UserCoupon userCoupon) {
        log.debug("添加用户优惠券列");
        userCouponService.insert(userCoupon);
        return JsonResp.ok(userCoupon);
    }

    /**
     * @param userCoupon
     * @return 返回值JsonResp
     * @修改用户优惠券列
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateUserCoupon(@RequestBody UserCoupon userCoupon) {
        log.debug("修改用户优惠券列");
        userCouponService.updateById(userCoupon);
        return JsonResp.ok(userCoupon);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找用户优惠券列
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserCoupon(Long id) {
        log.debug("查找用户优惠券列");
        UserCoupon userCoupon = userCouponService.selectById(id);
        return JsonResp.ok(userCoupon);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @邀请奖励统计
     */

    @SourceAuthority(SourceType.APP)
    @RequestMapping(value="/inviteFriend", method = RequestMethod.GET)
    public JsonResp inviteFriend(){
        log.debug("邀请统计奖励");
        User user = userService.findLoginUser();
        Map map = new HashMap();
        map.put("count", userCouponService.selectFriendCount(user.getId()));
        map.put("money", userCouponService.selectFriendMoney(user.getId()));
        return JsonResp.ok(map);
    }


    /**
     * @param
     * @return 返回值JsonResp
     * @根据登录用户查找优惠券
     */
    @RequestMapping(value = "/selectByUser", method = RequestMethod.GET)
    public JsonResp selectByUser() throws ParseException {
        log.debug("根据登录用户查找优惠券");
/*        //进来时判断用户优惠券是否过期

        //User user = userService.selectById(1);
        EntityWrapper<UserCoupon> ew = new EntityWrapper<>();
        ew.eq("user_id",user.getId());
        List<UserCoupon> userCouponList = userCouponService.selectList(ew);
        for(UserCoupon u:userCouponList){
            if(DateUtils.YYMMDDDate(new Date()).getTime()>u.getPastDatetime().getTime()){
                u.setStatus(3);
            }
        }
        userCouponService.updateBatchById(userCouponList);*/
        User user = userService.findLoginUser();
        return JsonResp.ok(userCouponService.selectUserCouponList(user.getId()));
    }


    /**
     * @param
     * @return 返回值JsonResp
     * @根据用户分页查找优惠券
     */
    @RequestMapping(value = "/findByUserPage", method = RequestMethod.GET)
    public JsonResp findByUserPage(Long userId,Integer pageNo,Integer pageSize){
        log.debug("根据用户分页查找优惠券");
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("userId",userId);
        Integer total = userCouponService.selectByUserCount(map);
        List<UserCoupon> list = userCouponService.findByUserPage(map);
        return JsonResp.ok(new PageDto(pageNo,pageSize,list,total));
    }





}
