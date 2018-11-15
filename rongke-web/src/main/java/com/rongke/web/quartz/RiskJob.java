package com.rongke.web.quartz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.ConstantUtil;
import com.rongke.model.AdminOperationLog;
import com.rongke.model.LoanOrder;
import com.rongke.model.PersonRecord;
import com.rongke.redis.CacheUtil;
import com.rongke.service.AdminOperationLogService;
import com.rongke.service.LoanOrderService;
import com.rongke.service.PersonRecordService;
import com.rongke.service.RiskService;
import com.rongke.utils.DateUtils;
import com.rongke.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;

@Component("RiskJob")
@Transactional
@CrossOrigin
public class RiskJob {

    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private RiskService riskService;
    @Autowired
    private LoanOrderService loanOrderService;
    @Autowired
    private AdminOperationLogService adminOperationLogService;
    @Autowired
    public PersonRecordService personRecordService;

    public void execute() {
        System.out.println("------------------------- risk job");
        String loanOrderId = this.cacheUtil.rightPop(ConstantUtil.RISK_TASK_LOAN_ORDER_ID);
        System.out.println("------------------------- risk job ---- loanOrderId : " + loanOrderId);
        if (StringUtil.isNotEmpty(loanOrderId)) {
//            this.riskService.runRiskRule(loanOrderId);
            LoanOrder currentLoanOrder = loanOrderService.selectById(loanOrderId);

            EntityWrapper<LoanOrder> loanOrderEw = new EntityWrapper();
            loanOrderEw.eq("user_id",currentLoanOrder.getUserId());
            loanOrderEw.eq("order_status","6");
            loanOrderEw.orderBy("real_pay_time",Boolean.FALSE);
            List<LoanOrder> loanOrders = loanOrderService.selectList(loanOrderEw);
            if(loanOrders != null && loanOrders.size()>0){
                LoanOrder loanOrder = loanOrders.get(0);
                Date realPayTime = loanOrder.getRealPayTime();
                Date limitPayTime = loanOrder.getLimitPayTime();
                Date inPassTime = DateUtils.dayAdd(3 + loanOrder.getAllowDays(), limitPayTime);
                if(limitPayTime.getTime() > realPayTime.getTime()){
                    if(currentLoanOrder.getOrderStatus()!=3){
                        currentLoanOrder.setOrderStatus(2);
                        currentLoanOrder.setPassTime(new Date());
                        boolean bo=loanOrderService.updateById(currentLoanOrder);
                        PersonRecord pr=personRecordService.selectById(2);
                        if(bo){
                            pr.setOutOrderCount(pr.getOutOrderCount()+1);
                        }
                        personRecordService.updateById(pr);
                    }
                    //记录该订单
                    AdminOperationLog adminOperationLog = new AdminOperationLog();
                    adminOperationLog.setDescription("三天内有正常还款，审核自动通过");
                    adminOperationLog.setCreateTime(new Date());
                    adminOperationLog.setParams(loanOrderId);
                    adminOperationLogService.insert(adminOperationLog);
                }
            }
        }
    }
}
