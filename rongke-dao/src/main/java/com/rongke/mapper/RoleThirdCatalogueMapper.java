package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.RoleThirdCatalogue;

import java.util.List;
import java.util.Map;

/**
 * @RoleThirdCatalogueMapper
 * @角色-功能关联Mapper
 * @version : Ver 1.0
 */
public interface RoleThirdCatalogueMapper extends BaseMapper<RoleThirdCatalogue>{

    List<RoleThirdCatalogue> findByPage(Map<String,Object> map);

    Integer findByPageCount(Map<String,Object> map);

    List<RoleThirdCatalogue> findAllByUser(Map<String,Object> map);

}
