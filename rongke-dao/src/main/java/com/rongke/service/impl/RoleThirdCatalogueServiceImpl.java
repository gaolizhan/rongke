package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.RoleThirdCatalogueMapper;
import com.rongke.model.RoleThirdCatalogue;
import com.rongke.service.RoleThirdCatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @RoleThirdCatalogueServiceImpl
 * @角色-功能关联ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class RoleThirdCatalogueServiceImpl extends ServiceImpl<RoleThirdCatalogueMapper, RoleThirdCatalogue> implements RoleThirdCatalogueService {

    @Autowired
    private RoleThirdCatalogueMapper roleThirdCatalogueMapper;

    public List<RoleThirdCatalogue> findByPage(Map<String,Object> map){
        return roleThirdCatalogueMapper.findByPage(map);
    }

    public Integer findByPageCount(Map<String,Object> map){
        return roleThirdCatalogueMapper.findByPageCount(map);
    }

    public List<RoleThirdCatalogue> findAllByUser(Map<String,Object> map){
        return roleThirdCatalogueMapper.findAllByUser(map);
    }
}
