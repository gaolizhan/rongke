package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserBlackMapper;
import com.rongke.model.UserBlack;
import com.rongke.service.UserBlackService;
import org.springframework.stereotype.Service;

@Service
public class UserBlackServiceImpl extends ServiceImpl<UserBlackMapper, UserBlack> implements UserBlackService {
}
