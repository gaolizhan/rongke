package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.AdminOperationLogMapper;
import com.rongke.model.AdminOperationLog;
import com.rongke.service.AdminOperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminOperationLogServiceImpl extends ServiceImpl<AdminOperationLogMapper, AdminOperationLog> implements AdminOperationLogService {
}
