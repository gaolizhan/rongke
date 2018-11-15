package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.ThirdCatalogue;

import java.util.List;
import java.util.Map;

/**
 * @ThirdCatalogueService
 * @三级目录Service
 * @version : Ver 1.0
 */
public interface ThirdCatalogueService extends IService<ThirdCatalogue>{
    List<ThirdCatalogue> findByPage(Map<String,Object> map);
    Integer findByPageCount(Map<String,Object> map);
    List<ThirdCatalogue> findAll();
    List<ThirdCatalogue> findAllSecond();
}
