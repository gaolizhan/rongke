package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.TbBaseInfoMapper;
import com.rongke.model.TbBaseInfo;
import com.rongke.service.TbBaseInfoService;
import org.springframework.stereotype.Service;

/**
 * @TbBaseInfoServiceImpl
 * @个人信息ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class TbBaseInfoServiceImpl extends ServiceImpl<TbBaseInfoMapper, TbBaseInfo> implements TbBaseInfoService {
}
