package com.rongke.mapper;

import com.rongke.model.UserPhoneRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * @version : Ver 1.0
 * @UserPhoneRecordMapper
 * @Mapper
 */
public interface UserPhoneRecordMapper extends BaseMapper<UserPhoneRecord> {

    void saveCall(List<UserPhoneRecord> userPhoneRecords);
}
