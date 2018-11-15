package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.CollectionRecord;

import java.util.List;

public interface CollectionRecordMapper extends BaseMapper<CollectionRecord> {
    List<CollectionRecord> selectAdminLoan(Long id);
}
