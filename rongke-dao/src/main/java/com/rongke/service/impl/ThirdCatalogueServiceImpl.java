package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.ThirdCatalogueMapper;
import com.rongke.model.ThirdCatalogue;
import com.rongke.service.ThirdCatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ThirdCatalogueServiceImpl
 * @三级目录ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class ThirdCatalogueServiceImpl extends ServiceImpl<ThirdCatalogueMapper, ThirdCatalogue> implements ThirdCatalogueService {

    @Autowired
    private ThirdCatalogueMapper thirdCatalogueMapper;

    public List<ThirdCatalogue> findByPage(Map<String,Object> map){
        return thirdCatalogueMapper.findByPage(map);
    }

    public Integer findByPageCount(Map<String,Object> map){
        return thirdCatalogueMapper.findByPageCount(map);
    }
    public List<ThirdCatalogue> findAll(){
        return thirdCatalogueMapper.findAll();
    }
    public List<ThirdCatalogue> findAllSecond(){
        return thirdCatalogueMapper.findAllSecond();
    }
}
