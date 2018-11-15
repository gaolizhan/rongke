package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.UserBasicMsg;
import com.rongke.model.UserPhoneList;
import com.rongke.service.UserBasicMsgService;
import com.rongke.service.UserPhoneListService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @UserPhoneListController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/userPhoneList")
@Transactional
@CrossOrigin
public class UserPhoneListController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserPhoneListService userPhoneListService;
    @Autowired
    private UserBasicMsgService userBasicMsgService;

    /**
     * @添加
     * @param userPhoneList
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addUserPhoneList(@RequestBody UserPhoneList userPhoneList){
        log.debug("添加");
        userPhoneListService.insert(userPhoneList);
        return JsonResp.ok(userPhoneList);
    }

    /**
     * @修改
     * @param userPhoneList
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateUserPhoneList(@RequestBody UserPhoneList userPhoneList){
        log.debug("修改");
        userPhoneListService.updateById(userPhoneList);
        return JsonResp.ok(userPhoneList);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */

    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserPhoneList(Long id){
        log.debug("查找");
        UserPhoneList userPhoneList = userPhoneListService.selectById(id);
        return JsonResp.ok(userPhoneList);
    }

    /**
     * @查询指定用户通讯录
     * @param userId
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findByUserPage", method = RequestMethod.GET)
    public JsonResp findByUserPage(Long userId,Integer pageNo,Integer pageSize,String phoneNo ){
        log.debug("查询指定用户通讯录");
        EntityWrapper<UserPhoneList> wrapper = new EntityWrapper<>();
        Map<String,Object>map=new HashMap<>();
        pageSize=50;
        map.put("pageSize",50);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("phone",phoneNo);
        map.put("userId",userId);
        List<UserPhoneList>userPhoneListList=userPhoneListService.selectUserPhoneList(map);
        Integer count=userPhoneListService.selectUserPhoneNum(map);

        return JsonResp.ok(new PageDto(pageNo,pageSize,userPhoneListList,count));


      /*  Page page = new Page(pageNo,pageSize);
        if(userId!=null&&!"".equals(userId)) {
            wrapper.eq("user_id", userId);
        }
        if(phoneNo!=null&&!"".equals(phoneNo)) {
            wrapper.like("phone", phoneNo);
        }
        wrapper.orderBy("call_times",false);
        Page page1 = userPhoneListService.selectPage(page,wrapper);
*/       // wrapper.setSqlSelect(" *,if(name='未知用户',0,1) AS t");
       // wrapper.orderBy("t",false);

    }


    /**
     * 查看紧急联系人信息
     * @return
     */
    @RequestMapping(value = "/selectEmInfo",method = RequestMethod.GET)
    public  JsonResp selectEmInfo(Long userId){
        EntityWrapper<UserBasicMsg>entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("user_id",userId);
        UserBasicMsg userBasicMsg=userBasicMsgService.selectOne(entityWrapper);
        List<UserPhoneList>userPhoneListList=new ArrayList<>();
        EntityWrapper<UserPhoneList>entityWrapper1=new EntityWrapper<>();
        entityWrapper1.eq("phone",userBasicMsg.getLinkPersonPhoneOne());
        UserPhoneList userPhoneList=userPhoneListService.selectOne(entityWrapper1);
        if (userPhoneList!=null){
        userPhoneList.setLink(userBasicMsg.getLinkPersonRelationOne());
        userPhoneListService.updateById(userPhoneList);


            userPhoneListList.add(userPhoneList);
        }

        EntityWrapper<UserPhoneList>entityWrapper2=new EntityWrapper<>();
        entityWrapper2.eq("phone",userBasicMsg.getLinkPersonPhoneTwo());
        UserPhoneList userPhoneList1=userPhoneListService.selectOne(entityWrapper2);
        if (userPhoneList1!=null){
        userPhoneList1.setLink(userBasicMsg.getLinkPersonRelationTwo());
        userPhoneListService.updateById(userPhoneList1);
            userPhoneListList.add(userPhoneList1);
        }

        return JsonResp.ok(userPhoneListList);


    }







}
