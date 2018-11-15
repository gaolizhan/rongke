package com.rongke.web.quartz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.model.LoanOrder;
import com.rongke.service.*;
import com.rongke.utils.DateUtils;
import com.rongke.web.lianpay.FenqiPayment;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7 0007.
 */
@Component("PayJob")
@Transactional
@CrossOrigin
public class PayJob {
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
    private UserCouponService userCouponService;

    public void printMessage() throws ParseException {
        log.info("扣款开始++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //找到所以要处理的订单
        EntityWrapper<LoanOrder> ewOrder = new EntityWrapper();
        List<Integer> list = new ArrayList<Integer>() {{
            add(3);
            add(4);
            add(5);
        }};
        ewOrder.in("order_status", list);
        List<LoanOrder> loanOrderList = loanOrderService.selectThisOrder();
        if (loanOrderList.size() != 0) {
            for (LoanOrder o : loanOrderList) {
                if (o.getOrderStatus() == 3) {
                    if (o.getLimitPayTime().getTime() == DateUtils.YYMMDDDate(new Date()).getTime()) {
                        log.info("我的扣款开始++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + o.getRepaymentNo());
                        log.info("userId" + o.getUserId().toString() + "no_order" + o.getLianRepayNum());
                        FenqiPayment.bankcardrepayment(o.getUserId().toString(), o.getLianRepayNum(), o.getRepaymentNo(), o.getNeedPayMoney().toString(), DateUtils.dateSimple(DateUtils.dayAdd(o.getLimitDays() - 1, o.getGiveTime())).toString(), o.getNoAgree(), o.getId().toString(), DateUtils.dateSimple(o.getUser().getGmtDatetime()).toString(), o.getUser().getPhone(), o.getUser().getUserName(), o.getUser().getIdNo());
                    }
                } else {
                    FenqiPayment.bankcardrepayment(o.getUserId().toString(), o.getLianRepayNum(), o.getRepaymentNo(), o.getNeedPayMoney().toString(), DateUtils.dateSimple(DateUtils.dayAdd(o.getLimitDays() - 1, o.getGiveTime())).toString(), o.getNoAgree(), o.getId().toString(), DateUtils.dateSimple(o.getUser().getGmtDatetime()).toString(), o.getUser().getPhone(), o.getUser().getUserName(), o.getUser().getIdNo());
                }
            }
        }
    }
}
