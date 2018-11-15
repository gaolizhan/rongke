package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.User;
import com.rongke.model.UserJindongAddress;
import com.rongke.service.UserJindongAddressService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @UserJindongAddressController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/userJindongAddress")
@Transactional
@CrossOrigin
public class UserJindongAddressController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserJindongAddressService userJindongAddressService;

    /**
     * @添加
     * @param userJindongAddress
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addUserJindongAddress(@RequestBody UserJindongAddress userJindongAddress){
        log.debug("添加");
        userJindongAddressService.insert(userJindongAddress);
        return JsonResp.ok(userJindongAddress);
    }

    /**
     * @修改
     * @param userJindongAddress
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateUserJindongAddress(@RequestBody UserJindongAddress userJindongAddress){
        log.debug("修改");
        userJindongAddressService.updateById(userJindongAddress);
        return JsonResp.ok(userJindongAddress);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserJindongAddress(Long id){
        log.debug("查找");
        UserJindongAddress userJindongAddress = userJindongAddressService.selectById(id);
        return JsonResp.ok(userJindongAddress);
    }
    /**
     * @分页查询京东认证用户地址
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findByAddressPage", method = RequestMethod.GET)
    public JsonResp findByAddressPage(Integer pageNo, Integer pageSize,Long userId){
        log.debug("分页查询京东认证用户地址");
        EntityWrapper<UserJindongAddress> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        Page page = userJindongAddressService.selectPage(new Page(pageNo,pageSize), wrapper);
        return JsonResp.ok(new PageDto(pageNo,pageSize,page.getRecords(),page.getTotal()));
    }



    /**
     * @分页查询京东认证用户
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findByPage", method = RequestMethod.GET)
    public JsonResp findByPage(Integer pageNo, Integer pageSize,String phone,String realName){
        log.debug("分页查询京东认证用户");
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("phone",phone);
        map.put("realName",realName);
        Integer count = userJindongAddressService.selectCount(map);
        List<User> list = userJindongAddressService.selectUser(map);

        return JsonResp.ok(new PageDto(pageNo,pageSize,list,count));
    }




}
