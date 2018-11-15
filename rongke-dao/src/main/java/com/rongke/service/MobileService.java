package com.rongke.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.LoanOrder;
import com.rongke.model.TongdunAudit;
import com.rongke.model.TongdunLog;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @MobileService
 * @Service
 */
public interface MobileService extends IService<TongdunLog> {
    Map<String,String> yongxunRun(String mobile, String cycle);

    TongdunLog TongDunRun(String userId, LoanOrder lo,Map<String, String> paramsMap);

    JSONObject tongDunGet(String reportId);

    List<TongdunLog> selectListForJob();

    JSONObject tongDunMonitor(LoanOrder loanOrder);
}
