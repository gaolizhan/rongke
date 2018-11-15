package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserBankMapper;
import com.rongke.model.UserBank;
import com.rongke.service.UserBankService;
import org.springframework.stereotype.Service;

/**
 * @version : Ver 1.0
 * @UserBankServiceImpl
 * @ServiceImpl
 */
@Service
public class UserBankServiceImpl extends ServiceImpl<UserBankMapper, UserBank> implements UserBankService {
}
