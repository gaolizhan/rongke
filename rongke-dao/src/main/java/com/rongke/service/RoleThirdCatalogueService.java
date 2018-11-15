package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.RoleThirdCatalogue;

import java.util.List;
import java.util.Map;

/**
 * @RoleThirdCatalogueService
 * @角色-功能关联Service
 * @version : Ver 1.0
 */
public interface RoleThirdCatalogueService extends IService<RoleThirdCatalogue>{
    List<RoleThirdCatalogue> findByPage(Map<String,Object> map);

    Integer findByPageCount(Map<String,Object> map);
    List<RoleThirdCatalogue> findAllByUser(Map<String,Object> map);
}
