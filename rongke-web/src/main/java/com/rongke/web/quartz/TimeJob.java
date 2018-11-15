package com.rongke.web.quartz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.model.LoanOrder;
import com.rongke.model.OrderExtend;
import com.rongke.model.ParamSetting;
import com.rongke.service.*;
import com.rongke.utils.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7 0007.
 */
@Component("TimeJob")
@Transactional
@CrossOrigin
public class TimeJob {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private LoanOrderService loanOrderService;
    @Autowired
    private ParamSettingService paramSettingService;
    @Autowired
    private MsgModelService msgModelService;
    @Autowired
    private OrderExtendService orderExtendService;
//    @Autowired
//    private SmsUtil smsUtil;

    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private SystemConfigService systemConfigService;

    public void printMessage() throws ParseException {
        List<LoanOrder> loanOrderList = loanOrderService.selectAllOrders();
        if (loanOrderList.size() != 0) {
            for (LoanOrder o : loanOrderList) {
                //利率
                EntityWrapper<ParamSetting> ewParam = new EntityWrapper();
                ewParam.eq("id", o.getParamSettingId());
                ewParam.eq("status", 1);
                ParamSetting paramSetting = paramSettingService.selectOne(ewParam);
                if(paramSetting != null){
                    //捕获异常，防止一个错误数据导致整个定时任务崩溃
                    try {
                        //没有容限期的时候
                        if (paramSetting.getAllowDays() == 0) {
                            //判断本金是否还完，还完也不在处理
                            BigDecimal realPayMoney = o.getRealPayMoney() == null ? new BigDecimal("0.00"):o.getRealPayMoney();

                            BigDecimal extendMoney = new BigDecimal("0.00");
                            EntityWrapper<OrderExtend> ewOe = new EntityWrapper();
                            ewOe.eq("order_id", o.getId());
                            ewOe.eq("status", 1);
                            List<OrderExtend> orderExtends = orderExtendService.selectList(ewOe);
                            if(orderExtends != null){
                                for (OrderExtend orderExtend : orderExtends) {
                                    extendMoney = extendMoney.add(orderExtend.getExtendMoney());
                                }
                            }
                            BigDecimal payAmountMoney = realPayMoney.subtract(extendMoney);
                            if(o.getBorrowMoney().compareTo(payAmountMoney)>0){
                                if (o.getOrderStatus() == 3) {
                                    //判断是否超出容限期
                                    if (o.getOverdueTime().getTime() < DateUtils.YYMMDDDate(new Date()).getTime()) {
                                        o.setOrderStatus(5);
                                        //超出容限期一天的费用
                                        BigDecimal money = (o.getBorrowMoney().subtract(payAmountMoney)).multiply(new BigDecimal(paramSetting.getOverduePercent() * 0.01)).setScale(1, BigDecimal.ROUND_HALF_UP);

                                        // 最大逾期费不能超过借款金额的两倍
                                        BigDecimal maxAmount = o.getBorrowMoney().multiply(BigDecimal.valueOf(2));
                                        if (maxAmount.compareTo(o.getOverdueMoney().add(money)) < 0) {
                                            money = BigDecimal.ZERO;
                                        }

                                        o.setOverdueDays(o.getOverdueDays() + 1);
                                        o.setOverdueMoney(o.getOverdueMoney().add(money));
                                        o.setWateMoney(o.getWateMoney().add(money));
                                        o.setNeedPayMoney(o.getNeedPayMoney().add(money));
                                        loanOrderService.updateById(o);
                                    }
                                } else if (o.getOrderStatus() == 5) {
                                    BigDecimal money = (o.getBorrowMoney().subtract(payAmountMoney)).multiply(new BigDecimal(paramSetting.getOverduePercent() * 0.01)).setScale(1, BigDecimal.ROUND_HALF_UP);

                                    // 最大逾期费不能超过借款金额的两倍
                                    BigDecimal maxAmount = o.getBorrowMoney().multiply(BigDecimal.valueOf(2));
                                    if (maxAmount.compareTo(o.getOverdueMoney().add(money)) < 0) {
                                        money = BigDecimal.ZERO;
                                    }

                                    o.setOverdueMoney(o.getOverdueMoney().add(money));
                                    o.setWateMoney(o.getWateMoney().add(money));
                                    o.setNeedPayMoney(o.getNeedPayMoney().add(money));
                                    o.setOverdueDays(o.getOverdueDays() + 1);
                                    loanOrderService.updateById(o);
                                    //发送逾期短信

                                }
                            }
                        }
                    }catch (Exception e){
                        log.error("计算逾期任务出现异常,错误userId =" +o.getUserId());
                    }
                }
            }
        }
    }
}
