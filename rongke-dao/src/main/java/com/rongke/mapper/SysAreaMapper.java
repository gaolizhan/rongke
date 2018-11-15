package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.SysArea;

import java.util.List;

public interface SysAreaMapper extends BaseMapper<SysArea> {
    List<SysArea> selectLikeName(String areaName);
}
