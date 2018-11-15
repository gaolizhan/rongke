package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.User;
import com.rongke.model.UserTaobaoAddress;
import com.rongke.service.UserService;
import com.rongke.service.UserTaobaoAddressService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @UserTaobaoAddressController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/userTaobaoAddress")
@Transactional
@CrossOrigin
public class UserTaobaoAddressController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserTaobaoAddressService userTaobaoAddressService;
    @Autowired
    private UserService userService;

    /**
     * @添加
     * @param userTaobaoAddress
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addUserTaobaoAddress(@RequestBody UserTaobaoAddress userTaobaoAddress){
        log.debug("添加");
        userTaobaoAddressService.insert(userTaobaoAddress);
        return JsonResp.ok(userTaobaoAddress);
    }

    /**
     * @修改
     * @param userTaobaoAddress
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateUserTaobaoAddress(@RequestBody UserTaobaoAddress userTaobaoAddress){
        log.debug("修改");
        userTaobaoAddressService.updateById(userTaobaoAddress);
        return JsonResp.ok(userTaobaoAddress);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserTaobaoAddress(Long id){
        log.debug("查找");
        UserTaobaoAddress userTaobaoAddress = userTaobaoAddressService.selectById(id);
        return JsonResp.ok(userTaobaoAddress);
    }


    /**
     * @分页查询淘宝认证用户
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findByPage", method = RequestMethod.GET)
    public JsonResp findByPage(Integer pageNo, Integer pageSize,String phone,String realName){
        log.debug("分页查询淘宝认证用户");
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("phone",phone);
        map.put("realName",realName);
        Integer count = userTaobaoAddressService.selectCount(map);
        List<User> list = userTaobaoAddressService.selectUser(map);

        return JsonResp.ok(new PageDto(pageNo,pageSize,list,count));
    }

    /**
     * @分页查询淘宝认证用户地址
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findByAddressPage", method = RequestMethod.GET)
    public JsonResp findByAddressPage(Integer pageNo, Integer pageSize,Long userId){
        log.debug("分页查询淘宝认证用户地址");
        EntityWrapper<UserTaobaoAddress> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        Page page = userTaobaoAddressService.selectPage(new Page(pageNo,pageSize), wrapper);
        return JsonResp.ok(new PageDto(pageNo,pageSize,page.getRecords(),page.getTotal()));
    }


    /**
     * @查询淘宝认证用户地址列表
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectAddressList", method = RequestMethod.GET)
    public JsonResp findByAddressPage(Long id){

        EntityWrapper<UserTaobaoAddress> wrapper = new EntityWrapper<>();
        if(id!=null&&!"".equals(id)){
        wrapper.eq("id",id);
    }
    List<UserTaobaoAddress> userTaobaoAddressesList=userTaobaoAddressService.selectList(wrapper);
       /*for(UserTaobaoAddress userTaobaoAddress:userTaobaoAddressesList){
           EntityWrapper<User> userEntityWrapper=new EntityWrapper<>();
           userEntityWrapper.eq("id",userTaobaoAddress.getUserId());
           User user=userService.selectOne(userEntityWrapper);
           userTaobaoAddress.setUser(user);
       }*/
     return  JsonResp.ok(userTaobaoAddressesList);
    }








}
