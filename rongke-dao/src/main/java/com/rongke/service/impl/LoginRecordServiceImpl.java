package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.LoginRecordMapper;
import com.rongke.model.LoginRecord;
import com.rongke.service.LoginRecordService;
import org.springframework.stereotype.Service;

/**
 * @LoginRecordServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class LoginRecordServiceImpl extends ServiceImpl<LoginRecordMapper, LoginRecord> implements LoginRecordService {
}
