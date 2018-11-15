package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.AppVersionInfoMapper;
import com.rongke.model.AppVersionInfo;
import com.rongke.service.AppVersionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppVersionInfoServiceImpl extends ServiceImpl<AppVersionInfoMapper, AppVersionInfo> implements AppVersionInfoService {

    @Autowired
    private  AppVersionInfoMapper appVersionInfoMapper;

    @Override
    public AppVersionInfo getLatestVersionInfo(String app) {
        return appVersionInfoMapper.getLatestVersionInfo(app);
    }
}
