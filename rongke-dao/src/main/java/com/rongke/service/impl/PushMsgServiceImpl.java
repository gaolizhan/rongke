package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.PushMsgMapper;
import com.rongke.model.PushMsg;
import com.rongke.service.PushMsgService;
import org.springframework.stereotype.Service;

/**
 * @PushMsgServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class PushMsgServiceImpl extends ServiceImpl<PushMsgMapper, PushMsg> implements PushMsgService {
}
