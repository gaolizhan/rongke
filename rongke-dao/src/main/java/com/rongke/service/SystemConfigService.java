package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.SystemConfig;

import java.util.List;

public interface SystemConfigService  extends IService<SystemConfig> {
    String selectValueByKey(String key);

    void updataByKey(SystemConfig sc);
    Integer updateListByConfigKey(List<SystemConfig> list);
}
