package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserRiskRecordMapper;
import com.rongke.model.UserRiskRecord;
import com.rongke.service.UserRiskRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRiskRecordServiceImpl extends ServiceImpl<UserRiskRecordMapper, UserRiskRecord> implements UserRiskRecordService {
}
