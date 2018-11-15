package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.Admin;
import com.rongke.model.Role;
import com.rongke.model.RoleThirdCatalogue;
import com.rongke.model.ThirdCatalogue;
import com.rongke.service.RoleService;
import com.rongke.service.RoleThirdCatalogueService;
import com.rongke.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @RoleThirdCatalogueController
 * @角色-功能关联Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/roleThirdCatalogue")
@Transactional
@CrossOrigin
public class RoleThirdCatalogueController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private RoleThirdCatalogueService roleThirdCatalogueService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService  roleService;

    /**
     * @添加角色-功能关联
     * @param roleThirdCatalogue
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addRoleThirdCatalogue(@RequestBody RoleThirdCatalogue roleThirdCatalogue){
        log.debug("添加角色-功能关联");
        roleThirdCatalogueService.insert(roleThirdCatalogue);
        return JsonResp.ok(roleThirdCatalogue);
    }

    /**
     * @修改角色-功能关联
     * @param roleThirdCatalogue
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateRoleThirdCatalogue(@RequestBody RoleThirdCatalogue roleThirdCatalogue){
        log.debug("修改角色-功能关联");
        roleThirdCatalogueService.updateById(roleThirdCatalogue);
        return JsonResp.ok(roleThirdCatalogue);
    }
    /**
     * @根据id查找角色-功能关联
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectRoleThirdCatalogue(Long id){
        log.debug("查找角色-功能关联");
        RoleThirdCatalogue roleThirdCatalogue = roleThirdCatalogueService.selectById(id);
        return JsonResp.ok(roleThirdCatalogue);
    }


    /**
     * @删除角色-功能关联
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/deleteOne", method = RequestMethod.GET)
    public JsonResp deleteRoleThirdCatalogue(Long id){
        log.debug("删除角色-功能关联");
        roleThirdCatalogueService.deleteById(id);
        return JsonResp.ok();
    }



    /**
     * @分页查询角色-功能关联
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findByPage", method = RequestMethod.POST)
    public JsonResp findByPage(Integer pageNo,Integer pageSize,Long roleId,@RequestBody ThirdCatalogue catalogue){
        log.debug("分页查询角色-功能关联");

        Map<String,Object> map = new HashMap<>();
        map.put("roleId",roleId);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("firstTitle",catalogue.getSecond().getFirst().getTitle());
        map.put("secondTitle",catalogue.getSecond().getTitle());
        map.put("thirdTitle",catalogue.getTitle());
        Integer total = roleThirdCatalogueService.findByPageCount(map);
        List<RoleThirdCatalogue> list = roleThirdCatalogueService.findByPage(map);
        return JsonResp.ok(new PageDto(pageNo,pageSize,list,total));
    }


    /**
     * @分页查询角色-功能关联
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findAllByUser", method = RequestMethod.GET)
    public JsonResp findAllByUser(String firstTitle,String secondTitle){
        log.debug("查询全部角色功能");
        Admin admin = userService.getThisLogin();
        Map<String,Object> map = new HashMap<>();
        map.put("roleId",admin.getRoleId());
        if (null != firstTitle) {
            map.put("firstTitle",firstTitle);
        }
        if (null != secondTitle) {
            map.put("secondTitle",secondTitle);
        }
        List<RoleThirdCatalogue> list = roleThirdCatalogueService.findAllByUser(map);
        return JsonResp.ok(list);
    }

    /**
     * @添加角色功能
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/addList", method = RequestMethod.POST)
    public JsonResp addList(String title,Long roleId,@RequestBody List<Long> ids){
        log.debug("添加角色功能");
        Role role = new Role();
        List<RoleThirdCatalogue> list = new ArrayList<>();
        if (null != roleId && !roleId.equals("")) {
            role.setId(roleId);
            EntityWrapper<RoleThirdCatalogue> wrapper = new EntityWrapper<>();
            wrapper.eq("role_id",roleId);
            roleThirdCatalogueService.delete(wrapper);
        }
        role.setTitle(title);
        roleService.insertOrUpdate(role);
        for (Long id : ids) {
            RoleThirdCatalogue catalogue = new RoleThirdCatalogue();
            catalogue.setRoleId(role.getId());
            catalogue.setThirdCatalogueId(id);
            list.add(catalogue);
        }
        roleThirdCatalogueService.insertBatch(list);
        return JsonResp.ok();
    }

}
