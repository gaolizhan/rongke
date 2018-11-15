package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.UserRiskRecord;

public interface UserRiskRecordMapper extends BaseMapper<UserRiskRecord> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(UserRiskRecord record);

    UserRiskRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserRiskRecord record);

    int updateByPrimaryKey(UserRiskRecord record);
}