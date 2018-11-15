package com.rongke.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.TongdunAudit;

import java.util.List;
import java.util.Map;

public interface TongdunAuditService extends IService<TongdunAudit> {
    JSONObject query(String reportId);
}
