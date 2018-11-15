package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.SysAreaMapper;
import com.rongke.model.SysArea;
import com.rongke.service.SysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysArea> implements SysAreaService {
    @Autowired
    private SysAreaMapper sysAreaMapper;
    @Override
    public List<SysArea> selectLikeName(String areaName) {
        return sysAreaMapper.selectLikeName(areaName);
    }
}
