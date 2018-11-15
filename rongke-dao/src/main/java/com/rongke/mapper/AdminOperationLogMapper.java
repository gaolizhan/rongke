package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.AdminOperationLog;

public interface AdminOperationLogMapper extends BaseMapper<AdminOperationLog> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(AdminOperationLog record);

    AdminOperationLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminOperationLog record);

    int updateByPrimaryKey(AdminOperationLog record);
}