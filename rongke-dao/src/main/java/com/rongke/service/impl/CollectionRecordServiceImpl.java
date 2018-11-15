package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.CollectionRecordMapper;
import com.rongke.model.CollectionRecord;
import com.rongke.service.CollectionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionRecordServiceImpl extends ServiceImpl<CollectionRecordMapper, CollectionRecord> implements CollectionRecordService {
    @Autowired
    private CollectionRecordMapper  collectionRecordMapper;

    @Override
    public List<CollectionRecord> selectAdminLoan(Long id) {
        return collectionRecordMapper.selectAdminLoan(id);
    }
}
