package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.model.Admin;
import com.rongke.model.Department;
import com.rongke.model.Role;
import com.rongke.service.*;
import com.rongke.utils.Md5;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version : Ver 1.0
 * @AdminController
 * @管理员Controller
 */
@RestController
@RequestMapping(value = "/api/admin")
@Transactional
@CrossOrigin
public class AdminController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private RoleService roleService;

    /**
     * @param admin
     * @return 返回值JsonResp
     * @添加管理员
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addAdmin(@RequestBody Admin admin) {
        log.debug("添加管理员");
        admin.setPassword(Md5.md5Encode(admin.getPassword()));
        adminService.insertOrUpdate(admin);
        return JsonResp.ok(admin);
    }

    /**
     * @param admin
     * @return 返回值JsonResp
     * @修改管理员
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateAdmin(@RequestBody Admin admin) {
        log.debug("修改管理员");
        adminService.updateById(admin);
        return JsonResp.ok(admin);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @修改管理员
     */
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public JsonResp deleteOne(Long id){
        log.debug("删除管理员");
        Admin admin = adminService.selectById(id);
        admin.setStatus(2);
        adminService.updateById(admin);
        return JsonResp.ok();
    }

    @Autowired
    private RoleThirdCatalogueService roleThirdCatalogueService;
    @RequestMapping(value = "/deleteRole", method = RequestMethod.GET)
    public JsonResp deleteRole(Long id){

        //新增加判断如果该角色已经和用户关联，那么提示"请先删除分配用户，在删除该角色"
        EntityWrapper<Admin> adminEw = new EntityWrapper();
        adminEw.eq("role_id",id);
        List<Admin> admins = adminService.selectList(adminEw);
        if(admins != null && admins.size()>0){
            return JsonResp.toFail("删除失败！！！请先删除被分配用户");
        }

        EntityWrapper ew1 = new EntityWrapper();
        ew1.eq("role_id",id);
        roleThirdCatalogueService.delete(ew1);
        EntityWrapper ew = new EntityWrapper();
        ew.eq("id",id);
        roleService.delete(ew);
        return JsonResp.ok();
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找管理员
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectAdmin(Long id) {
        log.debug("查找管理员");
        Admin admin = adminService.selectById(id);
        return JsonResp.ok(admin);
    }


    /**
     * @param
     * @return 返回值JsonResp
     * @根据id查找管理员
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public JsonResp login(String userName,String password) throws Exception {
        log.debug("登录");
        return JsonResp.ok(userService.loginByUserName(userName,password));

    }

    /**
     * @param
     * @return 返回值JsonResp
     * @根据id查找管理员
     */
    @RequestMapping(value = "/getThisLogin", method = RequestMethod.GET)
    public JsonResp getThisLogin(){
        log.debug("获取当前登录用户");
        Admin admin = userService.getThisLogin();
        return JsonResp.ok(admin);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @根据id查找管理员
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public JsonResp logout(){
        log.debug("退出登录");
        userService.logout();
        return JsonResp.ok();
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @分页查询管理员
     */
    @RequestMapping(value = "/selectPage", method = RequestMethod.GET)
    public JsonResp selectPage(Integer pageNo, Integer pageSize){
        EntityWrapper<Admin>adminEntityWrapper=new EntityWrapper<>();
        adminEntityWrapper.eq("status",1);
        Page page = adminService.selectPage(new Page(pageNo,pageSize), adminEntityWrapper);
        log.debug("分页查询管理员");

        List<Admin> list = page.getRecords();
        for(Admin admin:list){
            Role role=roleService.selectById(admin.getRoleId());
            admin.setRole(role);
            Department department=departmentService.selectById(admin.getDepartmentId());
            admin.setDepartment(department);
        }
        return JsonResp.dataPage(page);
    }



}
