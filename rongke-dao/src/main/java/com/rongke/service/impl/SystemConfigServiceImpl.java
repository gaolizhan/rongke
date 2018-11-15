package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.SystemConfigMapper;
import com.rongke.model.SystemConfig;
import com.rongke.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public String selectValueByKey(String key) {
        return systemConfigMapper.selectValueByKey(key);
    }

    @Override
    public void updataByKey(SystemConfig sc) {
        systemConfigMapper.updateByKy(sc);
    }

    public Integer updateListByConfigKey(List<SystemConfig> list){
        return systemConfigMapper.updateListByConfigKey(list);
    }

}
