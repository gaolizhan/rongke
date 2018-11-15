package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.User;
import com.rongke.model.UserLoginLog;
import com.rongke.service.UserLoginLogService;
import com.rongke.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @UserLoginLogController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/userLoginLog")
@Transactional
@CrossOrigin
public class UserLoginLogController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserLoginLogService userLoginLogService;
    @Autowired
    private UserService userService;

    /**
     * @添加
     * @param userLoginLog
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addUserLoginLog(@RequestBody UserLoginLog userLoginLog){
        log.debug("添加");
        User user = userService.findLoginUser();
        userLoginLog.setUserId(user.getId());
        userLoginLogService.insert(userLoginLog);
        return JsonResp.ok();
    }

    /**
     * @修改
     * @param userLoginLog
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateUserLoginLog(@RequestBody UserLoginLog userLoginLog){
        log.debug("修改");
        userLoginLogService.updateById(userLoginLog);
        return JsonResp.ok(userLoginLog);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserLoginLog(Long id){
        log.debug("查找");
        UserLoginLog userLoginLog = userLoginLogService.selectById(id);
        return JsonResp.ok(userLoginLog);
    }
    /**
     * 查询所有的app用户登录日志
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/selectAppLoginRecords",method = RequestMethod.GET)
    public JsonResp selectAdminLoginRecords(Page page,String phoneNumber){
/*
        Page page=userLoginLogService.selectPage(new Page(pageNo,pageSize));

        return JsonResp.ok(new PageDto(pageNo,pageSize,page.getRecords(),page.getTotal()));*/
        EntityWrapper<User>userEntityWrapper1=new EntityWrapper<>();

        Map<String,Object> map=new HashMap<String,Object>();
        if(phoneNumber!=null&&!"".equals(phoneNumber)){
            userEntityWrapper1.eq("phone",phoneNumber);
            User user1=userService.selectOne(userEntityWrapper1);
            map.put("userId",user1.getId());
        }else{
            map.put("userId","");
        }
        // User user1=userService.selectOne()


        map.put("pageNo",(page.getCurrent()-1)*page.getSize());
        map.put("pageSize",page.getSize());
        Integer count = userLoginLogService.selectNum1(map);
        List<UserLoginLog> list = userLoginLogService.selectList1(map);
        for(UserLoginLog userLoginLog:list){
            EntityWrapper<User> userEntityWrapper=new EntityWrapper<>();
            userEntityWrapper.eq("id",userLoginLog.getUserId());
            User user=userService.selectOne(userEntityWrapper);
            userLoginLog.setUser(user);
        }
      /*  Page page1=userLoginLogService.selectPage(new Page(pageNo,pageSize));
        page1.getRecords()*/
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(),list, count));


    }


   /**
     * 根据id删除一条日志
     * @param id
     * @return
             */
    @RequestMapping(value = "deleteOne",method = RequestMethod.GET)
    public JsonResp deleteOne(Long id){
        Boolean bool=userLoginLogService.deleteById(id);
        return  JsonResp.ok(bool);
    }




}
