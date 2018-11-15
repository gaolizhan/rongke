package com.rongke.mapper;

import com.rongke.model.ThirdCatalogue;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @ThirdCatalogueMapper
 * @三级目录Mapper
 * @version : Ver 1.0
 */
public interface ThirdCatalogueMapper extends BaseMapper<ThirdCatalogue>{

    List<ThirdCatalogue> findByPage(Map<String,Object> map);

    Integer findByPageCount(Map<String,Object> map);

    List<ThirdCatalogue> findAll();

    List<ThirdCatalogue> findAllSecond();

}
