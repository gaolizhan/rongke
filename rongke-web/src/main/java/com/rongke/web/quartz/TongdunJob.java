package com.rongke.web.quartz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.model.LoanOrder;
import com.rongke.model.TongdunAudit;
import com.rongke.model.TongdunLog;
import com.rongke.service.LoanOrderService;
import com.rongke.service.MobileService;
import com.rongke.service.SystemConfigService;
import com.rongke.service.TongdunAuditService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.text.ParseException;
import java.util.List;

@Component("TongdunJob")
@Transactional
@CrossOrigin
public class TongdunJob {
    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private TongdunAuditService tongdunAuditService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private LoanOrderService loanOrderService;
    public void queryResult() throws ParseException {
        log.info("***开始执行同盾贷前审核结果获取任务***");
        //获取编号list遍历list执行查询，存入持久层
        List<TongdunLog> list = mobileService.selectListForJob();
        for(TongdunLog l :list){
            log.info("现在查询报告编号为："+l.getReportId());
            JSONObject resultJO = mobileService.tongDunGet(l.getReportId());
            if(resultJO.getString("reason_code") !=  null){
            }else{
                TongdunAudit tongdunAudit = new TongdunAudit();
                tongdunAudit.setSuccess(resultJO.getString("success"));
                tongdunAudit.setFinalScore(resultJO.getString("final_score"));
                tongdunAudit.setFinalDecision(resultJO.getString("final_decision"));
                tongdunAudit.setReportId(resultJO.getString("report_id"));
                tongdunAudit.setDeviceType(resultJO.getString("device_type"));
                tongdunAudit.setApplyTime(resultJO.getString("apply_time"));
                tongdunAudit.setReportTime(resultJO.getString("report_time"));
                tongdunAudit.setRiskItems(resultJO.getString("rick_items"));
                tongdunAudit.setAddressDetect(resultJO.getString("address_detect"));
                tongdunAudit.setApplicationId(resultJO.getString("application_id"));
                tongdunAudit.setCreditScore(resultJO.getString("credit_score"));
                tongdunAudit.setUserId(l.getUserId());
                tongdunAuditService.insert(tongdunAudit);
                log.info(l.getReportId()+"查询成功。");
                Integer tdS = Integer.valueOf(systemConfigService.selectValueByKey("tongdun_score"));
                if(tdS <= Integer.valueOf(tongdunAudit.getFinalScore())){
                    EntityWrapper ew = new EntityWrapper();
                    ew.eq("user_id",l.getUserId());
                    LoanOrder lo = loanOrderService.selectOne(ew);
                    lo.setOrderStatus(9);
                    loanOrderService.update(lo,ew);
                }
            }
        }
        log.info("***同盾贷前审核定时任务结束***");
    }
}
