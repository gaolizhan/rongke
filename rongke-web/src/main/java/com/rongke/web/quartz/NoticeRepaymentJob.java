package com.rongke.web.quartz;

import com.rongke.commons.JpushClientUtil;
import com.rongke.commons.constant.MsgTemplate;
import com.rongke.commons.constant.PushTemplate;
import com.rongke.model.LoanOrder;
import com.rongke.model.User;
import com.rongke.service.LoanOrderService;
import com.rongke.service.PushMsgRecordService;
import com.rongke.service.UserService;
import com.rongke.utils.DateUtils;
import com.rongke.web.util.DateTimeUtils;
import com.rongke.web.util.YunpianSmsUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7 0007.
 */
@Transactional
public class NoticeRepaymentJob {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private LoanOrderService loanOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private PushMsgRecordService pushMsgRecordService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");

    public void sendRepaymentMessage() throws Exception {
        try {
            log.info("还款通知开始++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            long threeDaysLater = DateUtils.YYMMDDDate(DateUtils.dayAdd(3, new Date())).getTime();
            long twoDaysLater = DateUtils.YYMMDDDate(DateUtils.dayAdd(2, new Date())).getTime();
            long nowDate = DateUtils.YYMMDDDate(new Date()).getTime();
            //查询出待还款订单
            String content ="";
            List<LoanOrder> loanOrderList = loanOrderService.selectByOrderStatus(3);
            if(loanOrderList.size()!= 0){
                for(LoanOrder o :loanOrderList){
                    try {
                        User user = userService.selectById(o.getUserId());
                        if(o.getLimitPayTime().getTime()== threeDaysLater){
                            Date time = o.getLimitPayTime();
                            String[] arr = {DateTimeUtils.getFormatDate(time, DateTimeUtils.PART_DATE_FORMAT_CN)};
                            //三天后为还款日期
                            YunpianSmsUtil.sendSms(YunpianSmsUtil.YTempletEnum.NOTIFY_THIRD_DAY,user.getPhone(),arr);
                            //推送
                            List<String> userIds = new ArrayList<>();
                            userIds.add(user.getId().toString());
                            int status = JpushClientUtil.sendToAliasId(userIds,
                                    PushTemplate.NOTICE_REPAYMENT_PUSH_TITLE, PushTemplate.NOTICE_REPAYMENT_PUSH_TITLE, content, "");
                        }else if(o.getLimitPayTime().getTime()== twoDaysLater){
                            Date time = o.getLimitPayTime();
                            String[] arr = {DateTimeUtils.getFormatDate(time, DateTimeUtils.PART_DATE_FORMAT_CN)};
                            //三天后为还款日期
                            YunpianSmsUtil.sendSms(YunpianSmsUtil.YTempletEnum.NOTIFY_SECOND_DAY,user.getPhone(),arr);
                            //推送
                            List<String> userIds = new ArrayList<>();
                            userIds.add(user.getId().toString());
                            int status = JpushClientUtil.sendToAliasId(userIds,
                                    PushTemplate.NOTICE_REPAYMENT_PUSH_TITLE, PushTemplate.NOTICE_REPAYMENT_PUSH_TITLE, content, "");
                        }else if(o.getLimitPayTime().getTime()== nowDate){
                            //今天是还款日期
                            content = MessageFormat.format(MsgTemplate.NOW_DAY_NOTICE_REPAYMENT_SMS, "");
                            String[] arr ={};
                            YunpianSmsUtil.sendSms(YunpianSmsUtil.YTempletEnum.NOTIFY_NOW_DAY,user.getPhone(),arr);
                            //推送
                            List<String> userIds = new ArrayList<>();
                            userIds.add(user.getId().toString());
                            int status = JpushClientUtil.sendToAliasId(userIds,
                                    PushTemplate.NOTICE_REPAYMENT_PUSH_TITLE, PushTemplate.NOTICE_REPAYMENT_PUSH_TITLE, content, "");
                        }
                    }catch (Exception e){
                        log.error("发送还款短信异常,loanId="+o.getId(),e);
                    }
                }
            }
        }catch (Exception e){
            log.error("还款通知定时任务出错",e);
        }
    }


    public void sendExpireMessage() throws Exception {
        try {
            log.info("逾期提醒开始++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            //查询出逾期提醒订单
            String content ="";
            //增加条件只查询出逾期 1,3,5天的记录
            List<Integer> expireDayList = new ArrayList<>();
            expireDayList.add(1);
            expireDayList.add(3);
            expireDayList.add(5);
            List<LoanOrder> loanOrderList = loanOrderService.selectExpireOrderForExpireDate(expireDayList);
            if(loanOrderList.size()!=0){
                for(LoanOrder o :loanOrderList){
                    User user = userService.selectById(o.getUserId());
                    Integer overdueDays = o.getOverdueDays();

//                    content= MessageFormat.format(MsgTemplate.EXPIRE_SMS, user.getUserName(),overdueDays);
//                    //发送逾期短信
//                    DhSmsUtil.sendMessage(content,user.getPhone());
                    //逾期推送
                    List<String> userIds = new ArrayList<>();
                    userIds.add(user.getId().toString());
                    int status = JpushClientUtil.sendToAliasId(userIds,
                            PushTemplate.EXPIRE_ONE_DAY_PUSH_TITLE, PushTemplate.EXPIRE_ONE_DAY_PUSH_TITLE, content, "");
                }
            }
        }catch (Exception e){
            log.error("逾期提醒定时任务出错",e);
        }
    }

}
