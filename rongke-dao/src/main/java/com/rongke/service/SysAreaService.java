package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.SysArea;

import java.util.List;

public interface SysAreaService extends IService<SysArea> {
    List<SysArea> selectLikeName(String areaName);
}
