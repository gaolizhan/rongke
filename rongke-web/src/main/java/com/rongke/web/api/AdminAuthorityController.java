package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.mapper.AdminAuthorityMapper;
import com.rongke.model.AdminAuthority;
import com.rongke.service.AdminAuthorityService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @version : Ver 1.0
 * @AdminAuthorityController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/adminAuthority")
@Transactional
@CrossOrigin
public class AdminAuthorityController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private AdminAuthorityService adminAuthorityService;

    /**
     * @param adminAuthority
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addAdminAuthority(@RequestBody AdminAuthority adminAuthority) {
        log.debug("添加");
        adminAuthorityService.insert(adminAuthority);
        return JsonResp.ok(adminAuthority);
    }

    /**
     * @param adminAuthority
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateAdminAuthority(@RequestBody AdminAuthority adminAuthority) {
        log.debug("修改");
        adminAuthorityService.updateById(adminAuthority);
        return JsonResp.ok(adminAuthority);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectAdminAuthority(Long id) {
        log.debug("查找");
        AdminAuthority adminAuthority = adminAuthorityService.selectById(id);
        return JsonResp.ok(adminAuthority);
    }


}
