package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.model.User;
import com.rongke.model.UserAddress;
import com.rongke.service.UserAddressService;
import com.rongke.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by Administrator on 2018/8/15.
 */
@RestController
@RequestMapping(value="/api/userAddress")
//@Transactional
@CrossOrigin
public class UserAddressController {
    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private UserAddressService userAddressService;

    @RequestMapping(value="/getDefaultAddress", method = RequestMethod.GET)
    public JsonResp getDefaultAddress(){
        User user = userService.findLoginUser();

        EntityWrapper<UserAddress> commOrderEntityWrapper=new EntityWrapper<>();
        commOrderEntityWrapper.eq("user_id",user.getId());
        commOrderEntityWrapper.eq("default_sign","1");
        UserAddress userAddress = userAddressService.selectOne(commOrderEntityWrapper);
        return  new JsonResp().ok(userAddress);
    }

    @RequestMapping(value="/addAddress", method = RequestMethod.POST)
    public JsonResp addAddress(@RequestBody UserAddress userAddress){
        if(userAddress.getReceiverAddress()==null ||
                userAddress.getReceiverAddress()==null ||
                userAddress.getReceiverAddress()==null  ){
            new JsonResp().fail("请填写完整信息");
        }
        User user = userService.findLoginUser();
        userAddress.setUserId(user.getId());
        userAddress.setCreateTime(new Date());

        EntityWrapper<UserAddress> commOrderEntityWrapper=new EntityWrapper<>();
        commOrderEntityWrapper.eq("user_id",user.getId());
        UserAddress firstAddress = userAddressService.selectOne(commOrderEntityWrapper);
        if(firstAddress == null){
            userAddress.setDefaultSign(true);
        }else{
            userAddress.setDefaultSign(false);
        }
        userAddressService.insert(userAddress);
        return  new JsonResp().ok(userAddress);
    }

    @RequestMapping(value="/updateAddress", method = RequestMethod.POST)
    public JsonResp updateAddress(@RequestBody UserAddress userAddress){
        if(userAddress.getReceiverAddress()==null ||
                userAddress.getReceiverAddress()==null ||
                userAddress.getReceiverAddress()==null  ){
            new JsonResp().fa("请填写完整信息");
        }
        Date date = new Date();
        userAddress.setUpdateTime(date);
        boolean status = userAddressService.insertOrUpdate(userAddress);
        return  new JsonResp().ok(userAddress);
    }

    @RequestMapping(value="/setDefault", method = RequestMethod.GET)
    public JsonResp setDefault(Long userAddressId){
        User user = userService.findLoginUser();
        userAddressService.setDefault(userAddressId,user.getId());
        return  new JsonResp().ok("设置成功");
    }

    @RequestMapping(value="/selectPage", method = RequestMethod.GET)
    public JsonResp selectPage(Integer pageNo, Integer pageSize){
        User user = userService.findLoginUser();

        EntityWrapper<UserAddress> commOrderEntityWrapper=new EntityWrapper<>();
        commOrderEntityWrapper.eq("user_id",user.getId());
        Page page = userAddressService.selectPage(new Page(pageNo,pageSize,"create_time"), commOrderEntityWrapper);
        return  new JsonResp().ok(page);
    }
}
