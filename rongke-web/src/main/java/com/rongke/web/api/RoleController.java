package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.Role;
import com.rongke.service.RoleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RoleController
 * @角色Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/role")
@Transactional
@CrossOrigin
public class RoleController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private RoleService roleService;

    /**
     * @添加角色
     * @param role
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addRole(@RequestBody Role role){
        log.debug("添加角色");
        roleService.insert(role);
        return JsonResp.ok(role);
    }

    /**
     * @修改角色
     * @param role
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateRole(@RequestBody Role role){
        log.debug("修改角色");
        roleService.updateById(role);
        return JsonResp.ok(role);
    }
    /**
     * @根据id查找角色
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectRole(Long id){
        log.debug("查找角色");
        Role role = roleService.selectById(id);
        return JsonResp.ok(role);
    }

    /**
     * @查找全部角色
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectRoleAll", method = RequestMethod.GET)
    public JsonResp selectRoleAll(){
        log.debug("查找全部角色");
        EntityWrapper<Role> wrapper = new EntityWrapper<>();
        List<Role> roles = roleService.selectList(wrapper);
        return JsonResp.ok(roles);
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
        EntityWrapper<Role> wrapper =new EntityWrapper();
        Page page1 = roleService.selectPage(page,wrapper);
        return JsonResp.ok(new PageDto(pageNo,pageSize,page1.getRecords(),page1.getTotal()));
    }

//        System.out.println();
//    }


}
