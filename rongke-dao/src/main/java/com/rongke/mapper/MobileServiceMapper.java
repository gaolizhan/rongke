package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.TongdunAudit;
import com.rongke.model.TongdunLog;

import java.util.List;

public interface MobileServiceMapper extends BaseMapper<TongdunLog> {
    List<TongdunLog> selectListForJob();
}
