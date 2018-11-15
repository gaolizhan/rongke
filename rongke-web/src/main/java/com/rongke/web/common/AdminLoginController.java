package com.rongke.web.common;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * Created by chengwenwen on 2016/10/18.
 */
@RestController
@RequestMapping(value = "/api/adminLogin")
@CrossOrigin
public class AdminLoginController {
    private Logger log = Logger.getLogger(this.getClass());

    /*@Autowired
    private AdminService adminService;

    *//**
     * @查找当前登陆用户
     * @return 返回值JsonResp
     *//*
    @RequestMapping(value="/selectLoginAdmin", method = RequestMethod.GET)
    public JsonResp selectLoginAdmin(){
        log.debug("查找当前登陆管理员");
        Admin admin = adminService.findLoginAdmin();
        return JsonResp.ok(admin);
    }

    *//**
     * 登录  后台
     *
     * @return JsonResp
     * @throws UnsupportedEncodingException
     *//*
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public JsonResp login(String userName, String password) {
        log.debug("登录");
        EntityWrapper<Admin> ew =new EntityWrapper<>();
        ew.eq("user_name",userName);
        Admin admin = adminService.selectOne(ew);
        if(admin == null){
            return JsonResp.fa("账号不存在！");
        }else if(admin.getStatus() != 1){
            return JsonResp.fa("账号异常！");
        }else{
            return adminService.login(userName, password);
        }
    }

    *//**
     * 注销
     *//*
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public JsonResp logout(){
        log.debug("注销");
        adminService.logout();
        return JsonResp.ok();
    }*/
}
