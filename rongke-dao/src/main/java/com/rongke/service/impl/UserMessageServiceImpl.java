package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserMessageMapper;
import com.rongke.model.UserMessage;
import com.rongke.service.UserMessageService;
import org.springframework.stereotype.Service;

/**
 * @UserMessageServiceImpl
 * @用户推送消息ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {
}
