package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.mapper.DepartmentMapper;
import com.rongke.model.Admin;
import com.rongke.model.Department;
import com.rongke.model.Role;
import com.rongke.service.AdminService;
import com.rongke.service.DepartmentService;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @DepartmentController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/department")
@Transactional
@CrossOrigin
public class DepartmentController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private AdminService adminService;


    /**
     * @添加
     * @param department
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addDepartment(@RequestBody Department department){
        log.debug("添加");
        departmentService.insert(department);
        return JsonResp.ok(department);
    }

    /**
     * @修改
     * @param department
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateDepartment(@RequestBody Department department){
        log.debug("修改");
        departmentService.updateById(department);
        return JsonResp.ok(department);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectDepartment(Long id){
        log.debug("查找");
        Department department = departmentService.selectById(id);
        return JsonResp.ok(department);
    }

    /**
     * @分页查找角色
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findList", method = RequestMethod.GET)
    public JsonResp findList(Integer pageSize,Integer pageNo){
        log.debug("分页查找角色");
        Page page = new Page(pageNo,pageSize);
        EntityWrapper<Department> wrapper =new EntityWrapper();
        Page page1 = departmentService.selectPage(page,wrapper);
        return JsonResp.ok(new PageDto(pageNo,pageSize,page1.getRecords(),page1.getTotal()));
    }

    /**
     * 根据id删除
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteById",method = RequestMethod.GET)
    public JsonResp deleteById(Long id){
        EntityWrapper<Admin>adminEntityWrapper=new EntityWrapper<>();
        adminEntityWrapper.eq("department_id",id);
        List<Admin> admins = adminService.selectList(adminEntityWrapper);
        if(admins.size()>0){
            return JsonResp.toFail("删除失败！请先删除关联的用户。");
        }

        Boolean bool=departmentService.deleteById(id);
        return JsonResp.ok(bool);

    }
    /**
     * @查找所有部门
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectDepartmentAll", method = RequestMethod.GET)
    public JsonResp selectDepartmentAll(){
        log.debug("查找全部角色");
        EntityWrapper<Department> wrapper = new EntityWrapper<>();
        List<Department> department = departmentService.selectList(wrapper);
        return JsonResp.ok(department);
    }

}
