package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.CollectionRecord;

import java.util.List;

public interface CollectionRecordService extends IService<CollectionRecord> {
    List<CollectionRecord> selectAdminLoan(Long id);
}
