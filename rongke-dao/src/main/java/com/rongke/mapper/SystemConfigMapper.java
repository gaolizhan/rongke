package com.rongke.mapper;

import com.rongke.model.SystemConfig;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface SystemConfigMapper extends BaseMapper<SystemConfig>{
    String selectValueByKey(String key);

    void updateByKy(SystemConfig sc);

    Integer updateListByConfigKey(List<SystemConfig> list);
}
