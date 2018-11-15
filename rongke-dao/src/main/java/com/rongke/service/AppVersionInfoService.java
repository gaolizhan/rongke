package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.AppVersionInfo;

public interface AppVersionInfoService extends IService<AppVersionInfo> {
    AppVersionInfo getLatestVersionInfo(String app);
}
