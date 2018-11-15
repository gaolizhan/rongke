package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.UserPhoneRecord;

import java.util.List;

/**
 * @version : Ver 1.0
 * @UserPhoneRecordService
 * @Service
 */
public interface UserPhoneRecordService extends IService<UserPhoneRecord> {
    void saveCall(List<UserPhoneRecord> userPhoneRecords);
}
