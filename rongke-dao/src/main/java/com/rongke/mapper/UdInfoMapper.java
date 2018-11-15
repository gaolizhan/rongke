package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.UdInfo;

public interface UdInfoMapper extends BaseMapper<UdInfo> {
    int deleteByPrimaryKey(String udInfoId);

    int insertSelective(UdInfo record);

    UdInfo selectByPrimaryKey(String udInfoId);

    int updateByPrimaryKeySelective(UdInfo record);

    int updateByPrimaryKey(UdInfo record);
}