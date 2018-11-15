package com.rongke.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.AppVersionInfo;

public interface AppVersionInfoMapper extends BaseMapper<AppVersionInfo> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(AppVersionInfo record);

    AppVersionInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppVersionInfo record);

    int updateByPrimaryKeyWithBLOBs(AppVersionInfo record);

    int updateByPrimaryKey(AppVersionInfo record);

    AppVersionInfo getLatestVersionInfo(String app);
}