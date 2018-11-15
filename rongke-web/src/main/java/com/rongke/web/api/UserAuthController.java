package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.annotation.SourceAuthority;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.enums.SourceType;
import com.rongke.model.User;
import com.rongke.model.UserAuth;
import com.rongke.service.UserAuthService;
import com.rongke.service.UserService;
//import com.rongke.sms.SmsUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @UserAuthController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/userAuth")
@Transactional
@CrossOrigin
public class UserAuthController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserService userService;
//    @Autowired
//    private SmsUtil smsUtil;

    /**
     * @添加
     * @param userAuth
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addUserAuth(@RequestBody UserAuth userAuth){
        log.debug("添加");
        userAuthService.insert(userAuth);
        return JsonResp.ok(userAuth);
    }

    /**
     * @修改
     * @param userAuth
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateUserAuth(@RequestBody UserAuth userAuth){
        log.debug("修改");
        userAuthService.updateById(userAuth);
        return JsonResp.ok(userAuth);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserAuth(Long id){
        log.debug("查找");
        UserAuth userAuth = userAuthService.selectById(id);
        return JsonResp.ok(userAuth);
    }

    /**
     * @认证表
     * @param
     * @return 返回值JsonResp
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value="/selectAll", method = RequestMethod.GET)
    public JsonResp selectAll(){
        log.debug("认证表");
        User user = userService.findLoginUser();
        EntityWrapper<UserAuth> ew = new EntityWrapper<>();
        ew.eq("user_id",user.getId());
        UserAuth userAuth = userAuthService.selectOne(ew);
        Map map = new HashMap();
        map.put("socre",user.getAuthScore());
        map.put("auth",userAuth);
        return JsonResp.ok(map);
    }

    /**
     * @是否已缴30元
     * @param
     * @return 返回值JsonResp
     */

    @RequestMapping(value="/isPay30", method = RequestMethod.GET)
    public JsonResp isPay30(){
        User user = userService.findLoginUser();
        EntityWrapper<UserAuth> ew = new EntityWrapper<>();
        ew.eq("user_id",user.getId());
        UserAuth userAuth = userAuthService.selectOne(ew);
        return JsonResp.ok(userAuth.getAuthFee());
    }

    @RequestMapping(value = "/selectAllByPage",method = RequestMethod.GET)
    public JsonResp selectAllByPage(Page page,String userName,String phone){
        EntityWrapper<User>userEntityWrapper=new EntityWrapper<>();
        if (userName!=null&&!"".equals(userName)){
            userEntityWrapper.eq("user_name",userName);
        }
        if (phone!=null&&!"".equals(phone)){
            userEntityWrapper.eq("phone",phone);
        }
        List<User>userList=userService.selectList(userEntityWrapper);
        List<Long>ids=new ArrayList<>();
        for (User user:userList){
            ids.add(user.getId());
        }


        EntityWrapper<UserAuth>entityWrapper=new EntityWrapper<>();
        if (ids.size()>0){
            entityWrapper.in("user_id",ids);
        }
        Page<UserAuth> userAuthPage=userAuthService.selectPage(page,entityWrapper);
        for (UserAuth userAuth:userAuthPage.getRecords()){
            User user=userService.selectById(userAuth.getUserId());
            userAuth.setUser(user);
        }
        return JsonResp.dataPage(userAuthPage);

    }

    /**
     * 后台设置认证费用已缴
     * @return
     */
    @RequestMapping(value = "/setIsPay",method = RequestMethod.GET)
    public JsonResp setIsPay(Long id){
        UserAuth userAuth=userAuthService.selectById(id);
        userAuth.setAuthFee(1);
        userAuthService.updateById(userAuth);

        User user=userService.selectById(userAuth.getUserId());
        user.setAuthStatus(1);
        user.setIsPay(0);
        userService.updateById(user);

     /*  String content=user.getUserName();
       smsUtil.smsNotification(user.getPhone(),"3514",content);*/


        return JsonResp.ok();
    }

    @RequestMapping(value = "/selectAll1",method = RequestMethod.GET)
    public JsonResp selectAll1(Page page,String userName,String phone){
        //EntityWrapper<UserAuth>=new EntityWrapper<>();
        Map<String,Object>map=new HashMap<>();
        map.put("userName",userName);
        map.put("phone",phone);
        Integer pageNo=page.getCurrent();
        Integer pageSize=page.getSize();
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        List<UserAuth>userAuthList=userAuthService.selectAll(map);
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), userAuthList, userAuthList.size()));


    }



}
