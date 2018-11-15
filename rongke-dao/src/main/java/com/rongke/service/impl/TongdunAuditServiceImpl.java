package com.rongke.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.TongdunAuditMapper;
import com.rongke.model.TongdunAudit;
import com.rongke.service.HttpClientUtil;
import com.rongke.service.TongdunAuditService;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TongdunAuditServiceImpl extends ServiceImpl<TongdunAuditMapper, TongdunAudit> implements TongdunAuditService {
    @Autowired
    private TongdunAuditMapper tongdunAuditMapper;

    @Override
    public JSONObject query(String reportId) {
        String url = "https://apitest.tongdun.cn/preloan/report/v9?partner_code=demo&partner_key=21232f297a57a5a743894a0e4a801fc3&app_name=web_demo&report_id="+reportId;
        String result = HttpClientUtil.sendGetSSLRequest(url,"UTF-8");
        return JSON.parseObject(result);
    }

}
