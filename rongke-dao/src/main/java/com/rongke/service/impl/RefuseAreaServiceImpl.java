package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.RefuseAreaMapper;
import com.rongke.mapper.SysAreaMapper;
import com.rongke.model.RefuseArea;
import com.rongke.model.SysArea;
import com.rongke.service.RefuseAreaService;
import com.rongke.service.SysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefuseAreaServiceImpl extends ServiceImpl<RefuseAreaMapper, RefuseArea> implements RefuseAreaService {
}
