package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.mapper.AdminCatalogMapper;
import com.rongke.model.AdminCatalog;
import com.rongke.service.AdminCatalogService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @version : Ver 1.0
 * @AdminCatalogController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/adminCatalog")
@Transactional
@CrossOrigin
public class AdminCatalogController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private AdminCatalogService adminCatalogService;

    /**
     * @param adminCatalog
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addAdminCatalog(@RequestBody AdminCatalog adminCatalog) {
        log.debug("添加");
        adminCatalogService.insert(adminCatalog);
        return JsonResp.ok(adminCatalog);
    }

    /**
     * @param adminCatalog
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateAdminCatalog(@RequestBody AdminCatalog adminCatalog) {
        log.debug("修改");
        adminCatalogService.updateById(adminCatalog);
        return JsonResp.ok(adminCatalog);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectAdminCatalog(Long id) {
        log.debug("查找");
        AdminCatalog adminCatalog = adminCatalogService.selectById(id);
        return JsonResp.ok(adminCatalog);
    }


}
