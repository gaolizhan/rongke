package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.ConstraintPaymentRecordMapper;
import com.rongke.model.ConstraintPaymentRecord;
import com.rongke.service.ConstraintPaymentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConstraintPaymentRecordServiceImpl extends ServiceImpl<ConstraintPaymentRecordMapper, ConstraintPaymentRecord> implements ConstraintPaymentRecordService {
    @Autowired
    private ConstraintPaymentRecordMapper constraintPaymentRecordMapper;
}
