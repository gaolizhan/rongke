package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.MsgModelMapper;
import com.rongke.model.MsgModel;
import com.rongke.service.MsgModelService;
import org.springframework.stereotype.Service;

/**
 * @MsgModelServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class MsgModelServiceImpl extends ServiceImpl<MsgModelMapper, MsgModel> implements MsgModelService {
}
