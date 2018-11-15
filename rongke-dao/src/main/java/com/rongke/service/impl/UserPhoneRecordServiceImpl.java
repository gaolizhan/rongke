package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserPhoneRecordMapper;
import com.rongke.model.UserPhoneRecord;
import com.rongke.service.UserPhoneRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version : Ver 1.0
 * @UserPhoneRecordServiceImpl
 * @ServiceImpl
 */
@Service
public class UserPhoneRecordServiceImpl extends ServiceImpl<UserPhoneRecordMapper, UserPhoneRecord> implements UserPhoneRecordService {
    @Autowired
    private UserPhoneRecordMapper userPhoneRecordMapper;
    @Override
    public void saveCall(List<UserPhoneRecord> userPhoneRecords) {
        userPhoneRecordMapper.saveCall(userPhoneRecords);
    }
}
