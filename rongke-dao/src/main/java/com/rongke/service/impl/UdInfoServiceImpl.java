package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UdInfoMapper;
import com.rongke.model.UdInfo;
import com.rongke.service.UdInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UdInfoServiceImpl extends ServiceImpl<UdInfoMapper, UdInfo> implements UdInfoService {

    @Autowired UdInfoMapper udInfoMapper;
}
