package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.ConstraintPaymentRecord;

public interface ConstraintPaymentRecordMapper extends BaseMapper<ConstraintPaymentRecord> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(ConstraintPaymentRecord record);

    ConstraintPaymentRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConstraintPaymentRecord record);

    int updateByPrimaryKey(ConstraintPaymentRecord record);
}