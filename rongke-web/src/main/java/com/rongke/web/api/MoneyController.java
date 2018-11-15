package com.rongke.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JpushClientUtil;
import com.rongke.commons.JsonResp;
import com.rongke.commons.constant.MsgTemplate;
import com.rongke.commons.constant.PushTemplate;
import com.rongke.enums.EveryDayDataType;
import com.rongke.model.*;
import com.rongke.redis.CacheUtil;
import com.rongke.service.*;
import com.rongke.utils.DateUtils;
import com.rongke.utils.StringUtil;
import com.rongke.web.util.YunpianSmsUtil;
import com.rongke.web.yibaopay.YbApiHandler;
import com.rongke.yibaoApi.RemittanceApi;
import com.rongke.yibaoApi.RepaymentApi;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 支付接口类
 * Created by bin on 2017/3/23.
 */
@CrossOrigin
@RestController
@Transactional
@RequestMapping(value = "/api/money")
public class MoneyController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private LoanOrderService loanOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private PersonRecordService personRecordService;
    @Autowired
    private MsgModelService msgModelService;
    @Autowired
    private OrderExtendService orderExtendService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private ConstraintPaymentRecordService constraintPaymentRecordService;
    @Autowired
    private EveryDayDataService everyDayDataService;

    @RequestMapping(value = "/receivePayResult")
    @ResponseBody
    public void receivePayResult(HttpServletRequest request,HttpServletResponse resp) {

        String result = "success";
        String json = JSON.toJSONString(request.getParameterMap());
        log.info("获取合利宝异步返回:"+json);

        if (StringUtils.equals(request.getParameter("rt9_orderStatus"), "INIT") || StringUtils
                .equals("rt9_orderStatus", "DOING") || StringUtils.isEmpty(request.getParameter("rt9_orderStatus"))) {
            result =  "合利宝返回通信失败，不处理";
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if ("SUCCESS".equals(request.getParameter("rt9_orderStatus"))) {
            String requestno = request.getParameter("rt5_orderId");
            String str = cacheUtil.get(requestno);
            if(StringUtil.isEmpty(str)){
                log.error("缓存不存在"+requestno);
                return;
            }
            log.info("进入支付异步通知数据接收处理=====" + str + "=================合利宝");
            String orderId = str.split(",")[0];
            String money = str.split(",")[1];
            String type = str.split(",")[2];
            LoanOrder loanOrder = loanOrderService.selectById(orderId);
            User user = userService.selectById(loanOrder.getUserId());
            if (type.equals("1")) {
                //还款
                log.error("商户更新订单为成功，处理自己的业务逻辑");
                if (loanOrder != null && loanOrder.getOrderStatus() != 6) {
                    loanOrder.setOrderStatus(6);
                    try {
                        if (DateUtils.YYMMDDDate(new Date()).getTime() > loanOrder.getOverdueTime().getTime()) {
                            loanOrder.setPayStatus(2);//超出容限期还款
                            user.setMoney(user.getMoney().add(loanOrder.getBorrowMoney()));
                        } else {
                            loanOrder.setPayStatus(1);//正常还款
                            user.setMoney(user.getMoney().add(loanOrder.getBorrowMoney()));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    loanOrder.setRealPayMoney(loanOrder.getRealPayMoney().add(new BigDecimal(money)));
                    loanOrder.setRealPayTime(new Date());
                    loanOrderService.updateById(loanOrder);
                    user.setIsPay(0);
                    user.setIsOld(1);
                    userService.updateById(user);
                    //统计
                    EntityWrapper<PersonRecord> ew = new EntityWrapper();
                    PersonRecord personRecord = personRecordService.selectOne(ew);
                    personRecord.setOverOrderCount(personRecord.getOverOrderCount() + 1);
                    personRecordService.updateById(personRecord);

                    //发送账单已处理短信，和推送
                    try {
                        log.info("账单处理完成，发送提醒消息" + user.getId());
                        YunpianSmsUtil.sendSms(YunpianSmsUtil.YTempletEnum.ORDER_SETTLED, user.getPhone(),
                                null);
                        List<String> userIds = new ArrayList<>();
                        userIds.add(user.getId().toString());
                        int status = JpushClientUtil.sendToAliasId(userIds, PushTemplate.PAYMENT_SUCCESS_PUSH_TITLE,
                                PushTemplate.PAYMENT_SUCCESS_PUSH_TITLE, MsgTemplate.PAYMENT_SUCCESS_SMS, "");
                    } catch (Exception e) {
                        log.error("发送还款成功消息异常", e);
                    }
                }
            } else if (type.equals("2")) {
                //续期
                String extendId = str.split(",")[3];
                OrderExtend orderExtend = orderExtendService.selectById(extendId);
                log.error("商户更新订单为成功，处理自己的业务逻辑");
                if (orderExtend.getStatus() != 1) {

                    // 生成续期订单
                    LoanOrder extendOrder =  createExtendOrder(loanOrder,orderExtend);
                    if(extendOrder!= null){
                        // 修改老订单变成已完成
                        finishOldOrder(loanOrder,orderExtend);
                    }
                }
            }
            ServletOutputStream out = null;
            try {
                out = resp.getOutputStream();
                out.print(result);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 合利宝代付回调
     * @param request
     * @return
     * @throws RuntimeException
     */
    @RequestMapping(value = "/transferNotify", method = RequestMethod.POST)
    @Transactional(rollbackFor = RuntimeException.class)
    public synchronized String transferNotify(HttpServletRequest request) {
        try {
            String responseStr = JSON.toJSONString(request.getParameterMap());
            log.info("<-- 合利宝代付回调:"+responseStr +"-->");
            if(!"0000".equals(request.getParameter("rt2_retCode"))){
                return "failed";
            }
            if (StringUtils.isEmpty(request.getParameter("sign")) ) {
                return "failed";
            }
            String msg = request.getParameter("rt8_reason");
            String status = request.getParameter("rt7_orderStatus");
            String requestNo = request.getParameter("rt5_orderId");
            String orderId = requestNo.substring(0, requestNo.length()-1);
            log.info("合利宝代付回调 orderId: " + orderId +"; status: "+ status + "; msg: " + msg);
            if("RECEIVE".equals(status) || "INIT".equals(status) || "DOING".equals(status)){
                // 打款中
                return "failed";
            }
            //账单状态 1审核中 2待打款 3待还款 4容限期中 5已逾期 6已还款 7审核失败 8坏账
            //打款状态 0未打款 1打款还未成功 2打款成功 3打款失败 4退款状态
            LoanOrder loanOrder = loanOrderService.selectById(orderId);
            //判断账单状态
            if(!"9".equals(loanOrder.getOrderStatus().toString()) || !"1".equals(loanOrder.getGiveStatus().toString())){
                log.info("<-- 合利宝代付回调 打款,账单状态不正确，orderId:"+orderId+"; orderStatus:"+loanOrder.getOrderStatus()
                        +"; giveStatus:"+loanOrder.getGiveStatus()+"-->");
                return "failed";
            }
            if("SUCCESS".equals(status)){
                log.info("<-- 合利宝代付回调 打款成功，orderId"+orderId+"-->");
                // 打款成功
                loanOrder.setGiveStatus(2);
                loanOrder.setOrderStatus(3);
                loanOrder.setGiveTime(new Date());
                //改变时间
                loanOrder.setLimitPayTime(DateUtils.dayAdd(loanOrder.getLimitDays() - 1, new Date()));
                loanOrder.setOverdueTime(DateUtils.dayAdd(loanOrder.getLimitDays() + loanOrder.getAllowDays() - 1, new Date()));
                loanOrderService.updateById(loanOrder);
                //统计
                User user = userService.selectById(loanOrder.getUserId());
                try {
                    if(user.getIsOld()==0){
                        everyDayDataService.addEveryDayData(EveryDayDataType.TYPE.LENDING_NUM.getKey(),user.getChannelId()==null?0:user.getChannelId());
                        log.info("合利宝代付回调放款统计+1");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                EntityWrapper<PersonRecord> ew = new EntityWrapper<>();
                PersonRecord personRecord = personRecordService.selectOne(ew);
                personRecord.setOutOrderCount(personRecord.getOutOrderCount() + 1);
                personRecord.setOutMoney(personRecord.getOutMoney().add(loanOrder.getRealMoney()));
                personRecordService.updateById(personRecord);
                //有渠道商引进的，记录渠道商信息
                if (user.getChannelId() != null) {
                    //老用户不再算利润
                    EntityWrapper<LoanOrder> ewOrder = new EntityWrapper<>();
                    ewOrder.eq("order_status", 6);
                    ewOrder.eq("user_id", user.getId());
                    List<LoanOrder> loanOrderList = loanOrderService.selectList(ewOrder);
                    if (loanOrderList.size() == 0) {
                        EntityWrapper<Channel> cwrapper = new EntityWrapper<>();
                        cwrapper.eq("id", user.getChannelId());
                        Channel channel = channelService.selectOne(cwrapper);

                        BigDecimal proportion = StringUtil.isEmpty(channel.getProportion()) ? BigDecimal.ZERO : new BigDecimal(channel.getProportion());
                        BigDecimal profit = StringUtil.isEmpty(channel.getProfit()) ? BigDecimal.ZERO : new BigDecimal(channel.getProfit());
                        BigDecimal outMoney = StringUtil.isEmpty(channel.getOutMoney()) ? BigDecimal.ZERO : new BigDecimal(channel.getOutMoney());

                        BigDecimal bigDecimal = proportion.multiply(loanOrder.getBorrowMoney()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        channel.setProfit(profit.add(bigDecimal).toString());
                        channel.setOutMoney(outMoney.add(loanOrder.getRealMoney()).toString());

                        channelService.updateById(channel);

                        loanOrder.setChannelProfit(bigDecimal);
                        loanOrderService.updateById(loanOrder);
                    }
                }
                return "success";
            }
            if("FAIL".equals(status) || "REFUND".equals(status)){
                // 打款失败
                loanOrder.setOrderStatus(9);
                loanOrder.setGiveStatus(3);
                loanOrderService.updateById(loanOrder);
                return "success";
            }
        } catch (Exception e) {
           log.error("transferNotify error", e);
        }
        return "failed";
    }


    /**
     * 生成新的续期订单
     * @param oldOrder
     * @return
     */
    private LoanOrder createExtendOrder(LoanOrder oldOrder,OrderExtend orderExtend){
        LoanOrder extendOrder = new LoanOrder();
        BeanUtils.copyProperties(oldOrder,extendOrder);

        Date now = new Date();
        // 设置新的到期时间
        Date oldLimitPayTime = oldOrder.getLimitPayTime();
        Date newLimitPayTime = DateUtils.dayAdd(orderExtend.getExtendDays() - 1 + oldOrder.getOverdueDays(),oldLimitPayTime);
        // 设置新的容期时间时间
        Date overdueTime = oldOrder.getOverdueTime();
        Date newOverdueTime = DateUtils.dayAdd(orderExtend.getExtendDays() - 1 + oldOrder.getOverdueDays(),overdueTime);

        // 设置订单待还款
        // 设置新的到期时间和容期时间
        // 设置成续期订单
        // 应还金额变成实借金额
        // 综合费用已经扣除 需致0
        extendOrder.setId(null);
        extendOrder.setPassTime(now);
        extendOrder.setGiveTime(now);
        extendOrder.setGmtDatetime(now);
        extendOrder.setOrderStatus(3);
        extendOrder.setType(1);
        extendOrder.setExtendType(2);
        extendOrder.setNeedPayMoney(extendOrder.getBorrowMoney());
        extendOrder.setExtendNum(extendOrder.getExtendNum()+1);
        extendOrder.setLimitDays(orderExtend.getExtendDays()-1);
        extendOrder.setLimitPayTime(newLimitPayTime);
        extendOrder.setOverdueTime(newOverdueTime);
        extendOrder.setWateMoney(new BigDecimal(0));
        extendOrder.setOverdueDays(0);
        extendOrder.setAllowDays(0);
        extendOrder.setAllowMoney(new BigDecimal(0));
        extendOrder.setTdScore(oldOrder.getTdScore()==null?"":oldOrder.getTdScore());
        if(loanOrderService.insert(extendOrder)) {
            return extendOrder;
        }

        return null;
    }

    /**
     * 老订单状态还款
     * @param oldOrder
     * @param orderExtend
     * @return
     */
    private void finishOldOrder(LoanOrder oldOrder,OrderExtend orderExtend){
        // 设置已还款
        // 设置已逾期订单
        // 实还金额: 原实还金额+续期费
        // 老单逾期金额要
        oldOrder.setOrderStatus(6);
        oldOrder.setExtendType(1);
        oldOrder.setRealPayMoney(oldOrder.getRealPayMoney().add(orderExtend.getExtendMoney()));
        oldOrder.setOverdueDays(0);
        oldOrder.setOverdueMoney(new BigDecimal(0));
        oldOrder.setAllowDays(0);
        oldOrder.setAllowMoney(new BigDecimal(0));
        oldOrder.setRealPayTime(new Date());
        try {
            if (DateUtils.YYMMDDDate(new Date()).getTime() > oldOrder.getOverdueTime().getTime()) {
                oldOrder.setPayStatus(2);//超出容限期还款
            } else {
                oldOrder.setPayStatus(1);//正常还款
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        loanOrderService.updateById(oldOrder);
        // 修改逾期表订单状态
        orderExtend.setStatus(1);
        orderExtendService.updateById(orderExtend);
    }



    /**
     * @param
     * @return 返回值JsonResp
     * @易宝打款回调
     */
    @RequestMapping(value = "/paymentBackByYiBao", method = RequestMethod.POST)
    public synchronized JsonResp paymentBackByYiBao(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        log.error("易宝打款回调====================");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = RemittanceApi.remittanceNotifyCreVerify(request);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        log.error("=============================" + jsonObject.toString());
        String orderId = jsonObject.getString("orderId");
        log.error("=============================" + orderId);
        if ("S".equals(jsonObject.getString("status"))) {
            String batchNo = jsonObject.getString("batchNo");
            if (orderId.contains("DK")) {
                //打款
                LoanOrder loanOrder = loanOrderService.selectById(orderId.replaceAll("[a-zA-Z]", ""));
                log.error("=============================+++++++++" + loanOrder.getOrderStatus());
                if (loanOrder.getOrderStatus() == 2) {
                    loanOrder.setGiveStatus(2);
                    loanOrder.setOrderStatus(3);
                    loanOrder.setGiveTime(new Date());
                    //改变时间
                    loanOrder.setLimitPayTime(DateUtils.dayAdd(loanOrder.getLimitDays() - 1, new Date()));
                    loanOrder.setOverdueTime(DateUtils.dayAdd(loanOrder.getLimitDays() + loanOrder.getAllowDays() - 1, new Date()));
                    loanOrderService.updateById(loanOrder);

                    User user = userService.selectById(loanOrder.getUserId());
                    //统计
                    try {
                        if(user.getIsOld()==0){
                            everyDayDataService.addEveryDayData(EveryDayDataType.TYPE.LENDING_NUM.getKey(),user.getChannelId()==null?0:user.getChannelId());
                            log.info("易宝打款回调放款统计+1");
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    EntityWrapper<PersonRecord> ew = new EntityWrapper<>();
                    PersonRecord personRecord = personRecordService.selectOne(ew);
                    personRecord.setOutOrderCount(personRecord.getOutOrderCount() + 1);
                    personRecord.setOutMoney(personRecord.getOutMoney().add(loanOrder.getRealMoney()));
                    personRecordService.updateById(personRecord);

                    //有渠道商引进的，记录渠道商信息
                    if (user.getChannelId() != null) {
                        //老用户不再算利润
                        EntityWrapper<LoanOrder> ewOrder = new EntityWrapper<>();
                        ewOrder.eq("order_status", 6);
                        ewOrder.eq("user_id", user.getId());
                        List<LoanOrder> loanOrderList = loanOrderService.selectList(ewOrder);
                        if (loanOrderList.size() == 0) {
                            EntityWrapper<Channel> wrapper = new EntityWrapper<>();
                            wrapper.eq("id", user.getChannelId());
                            Channel channel = channelService.selectOne(wrapper);

                            BigDecimal proportion = StringUtil.isEmpty(channel.getProportion()) ? BigDecimal.ZERO : new BigDecimal(channel.getProportion());
                            BigDecimal profit = StringUtil.isEmpty(channel.getProfit()) ? BigDecimal.ZERO : new BigDecimal(channel.getProfit());
                            BigDecimal outMoney = StringUtil.isEmpty(channel.getOutMoney()) ? BigDecimal.ZERO : new BigDecimal(channel.getOutMoney());

                            BigDecimal bigDecimal = proportion.multiply(loanOrder.getBorrowMoney()).setScale(2, BigDecimal.ROUND_HALF_UP);
                            channel.setProfit(profit.add(bigDecimal).toString());
                            channel.setOutMoney(outMoney.add(loanOrder.getRealMoney()).toString());

                            channelService.updateById(channel);

                            loanOrder.setChannelProfit(bigDecimal);
                            loanOrderService.updateById(loanOrder);
                        }
                    }
                }
            }
            ServletOutputStream out = null;
            try {
                out = response.getOutputStream();
                out.print("S");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            //打款失败时
            if (orderId.contains("DK")) {
                LoanOrder loanOrder = loanOrderService.selectById(orderId.replaceAll("[a-zA-Z]", ""));
                loanOrder.setGiveStatus(3);
                loanOrderService.updateById(loanOrder);
            }
        }
        return JsonResp.ok();
    }

    /*** 还款回调
     ** @param
     * @return
     * */
    @RequestMapping(value = "/rePayOrderBackByYiBao", method = RequestMethod.POST)
    public synchronized void rePayOrderBackByYiBao(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        resp.setCharacterEncoding("UTF-8");
        log.error("进入支付异步通知数据接收处理======================易宝");
        Map<String, String> params = RepaymentApi.decodeParams(req);
        if ("PAY_SUCCESS".equals(params.get("status"))) {
            String requestno = params.get("requestno");
            String str = cacheUtil.get(requestno);
            log.error("进入支付异步通知数据接收处理=====" + str + "=================易宝");
            String orderId = str.split(",")[0];
            String money = str.split(",")[1];
            String type = str.split(",")[2];
            LoanOrder loanOrder = loanOrderService.selectById(orderId);
            User user = userService.selectById(loanOrder.getUserId());
            if (type.equals("1")) {
                //还款
                log.error("商户更新订单为成功，处理自己的业务逻辑");
                if (loanOrder != null && loanOrder.getOrderStatus() != 6) {
                    loanOrder.setOrderStatus(6);
                    try {
                        if (DateUtils.YYMMDDDate(new Date()).getTime() > loanOrder.getOverdueTime().getTime()) {
                            loanOrder.setPayStatus(2);//超出容限期还款
                            user.setMoney(user.getMoney().add(loanOrder.getBorrowMoney()));
                        } else {
                            loanOrder.setPayStatus(1);//正常还款
                            user.setMoney(user.getMoney().add(loanOrder.getBorrowMoney()));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    loanOrder.setRealPayMoney(loanOrder.getRealPayMoney().add(new BigDecimal(money)));
                    loanOrder.setRealPayTime(new Date());
                    loanOrderService.updateById(loanOrder);
                    user.setIsPay(0);
                    user.setIsOld(1);
                    userService.updateById(user);
                    //统计
                    EntityWrapper<PersonRecord> ew = new EntityWrapper();
                    PersonRecord personRecord = personRecordService.selectOne(ew);
                    personRecord.setOverOrderCount(personRecord.getOverOrderCount() + 1);
                    personRecordService.updateById(personRecord);

                    //发送账单已处理短信，和推送
                    try {
                        log.info("账单处理完成，发送提醒消息"+ user.getId());
                        YunpianSmsUtil.sendSms(YunpianSmsUtil.YTempletEnum.ORDER_SETTLED,user.getPhone(),null);
                        List<String> userIds = new ArrayList<>();
                        userIds.add(user.getId().toString());
                        int status = JpushClientUtil.sendToAliasId(userIds,
                                PushTemplate.PAYMENT_SUCCESS_PUSH_TITLE, PushTemplate.PAYMENT_SUCCESS_PUSH_TITLE, MsgTemplate.PAYMENT_SUCCESS_SMS, "");
                    }catch (Exception e){
                        log.error("发送还款成功消息异常",e);
                    }
                }
            } else if (type.equals("2")) {
                //续期
                String extendId = str.split(",")[3];
                OrderExtend orderExtend = orderExtendService.selectById(extendId);
                log.error("商户更新订单为成功，处理自己的业务逻辑");
                if (orderExtend.getStatus() != 1) {
                    loanOrder.setOrderStatus(3);
                    loanOrder.setExtendNum(loanOrder.getExtendNum() + 1);
                    loanOrder.setRealPayMoney(loanOrder.getRealPayMoney().add(new BigDecimal(money)));
                    loanOrder.setNeedPayMoney(loanOrder.getNeedPayMoney().subtract(loanOrder.getOverdueMoney()));
                    loanOrder.setWateMoney(loanOrder.getWateMoney().subtract(loanOrder.getOverdueMoney()));
                    //时间
                    loanOrder.setLimitPayTime(DateUtils.dayAdd(orderExtend.getExtendDays() - 1 + loanOrder.getOverdueDays(), loanOrder.getLimitPayTime()));
                    loanOrder.setOverdueTime(DateUtils.dayAdd(orderExtend.getExtendDays() - 1 + loanOrder.getOverdueDays(), loanOrder.getOverdueTime()));
                    //清除逾期的金额天数
                    loanOrder.setOverdueDays(0);
                    loanOrder.setOverdueMoney(new BigDecimal(0));
                    loanOrder.setAllowDays(0);
                    loanOrder.setAllowMoney(new BigDecimal(0));
                    loanOrderService.updateById(loanOrder);
                    orderExtend.setStatus(1);
                    orderExtendService.updateById(orderExtend);
                }
            }
            ServletOutputStream out = null;
            try {
                out = resp.getOutputStream();
                out.print("success");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*** 强制还款回调
     ** @param
     * @return
     * */
    @RequestMapping(value = "/constraintPaymentCallback", method = RequestMethod.POST)
    @Transactional
    public synchronized String constraintPaymentCallBack(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Enumeration en = req.getHeaderNames();
        while (en.hasMoreElements()){
            String h = String.valueOf(en.nextElement());
            log.info("----- " + h +" ------ " + req.getHeader(h));
        }

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        log.error("进入强制还款异步通知数据接收处理======================易宝");

        Map<String, String> params = YbApiHandler.decodeParams(req);

        EntityWrapper<ConstraintPaymentRecord> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("requestno", params.get("requestno"));
        ConstraintPaymentRecord constraintPaymentRecord = constraintPaymentRecordService.selectOne(entityWrapper);

        String recordStatus = constraintPaymentRecord.getStatus();
        if(!StringUtils.isEmpty(recordStatus) && "PAY_SUCCESS".equals(recordStatus)){
            log.info("重复收到扣款回调");
            return "SUCCESS";
        }

        if ("PAY_SUCCESS".equals(params.get("status"))) {
            String requestno = params.get("requestno");
            String str = cacheUtil.get(requestno);
            log.error("进入强制还款异步通知数据接收处理=====" + str + "=================易宝");
            String orderId = str.split(",")[0];
            String money = str.split(",")[1];
            String type = str.split(",")[2];
            LoanOrder loanOrder = loanOrderService.selectById(orderId);
            User user = userService.selectById(loanOrder.getUserId());

            log.error("商户更新订单为成功，处理自己的业务逻辑");
            if (loanOrder != null && loanOrder.getOrderStatus() != 6) {
                if(loanOrder.getNeedPayMoney().compareTo(new BigDecimal(money)) == 0){
                    loanOrder.setOrderStatus(6);
                    try {
                        if (DateUtils.YYMMDDDate(new Date()).getTime() > loanOrder.getOverdueTime().getTime()) {
                            loanOrder.setPayStatus(2);//超出容限期还款
                            user.setMoney(user.getMoney().add(loanOrder.getBorrowMoney()));
                        } else {
                            loanOrder.setPayStatus(1);//正常还款
                            user.setMoney(user.getMoney().add(loanOrder.getBorrowMoney()));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    loanOrder.setAutoPaymentMoney(loanOrder.getAutoPaymentMoney().add(new BigDecimal(money)));
                    loanOrder.setRealPayMoney(loanOrder.getRealPayMoney().add(new BigDecimal(money)));
                    loanOrder.setRealPayTime(new Date());
                    loanOrderService.updateById(loanOrder);
                    user.setIsPay(0);
                    user.setIsOld(1);
                    userService.updateById(user);
                    //统计
                    EntityWrapper<PersonRecord> ew = new EntityWrapper();
                    PersonRecord personRecord = personRecordService.selectOne(ew);
                    personRecord.setOverOrderCount(personRecord.getOverOrderCount() + 1);
                    personRecordService.updateById(personRecord);
                }else{
                    //没有还完,做统计
                    loanOrder.setAutoPaymentMoney(loanOrder.getAutoPaymentMoney().add(new BigDecimal(money)));
                    loanOrder.setNeedPayMoney(loanOrder.getNeedPayMoney().subtract(new BigDecimal(money)));
                    loanOrder.setRealPayMoney(loanOrder.getRealPayMoney().add(new BigDecimal(money)));
                    loanOrderService.updateById(loanOrder);
                }
            }
        }else{
            //支付失败,记录失败原因
            constraintPaymentRecord.setErrormsg(params.get("errormsg"));
        }
        //更新ConstraintPaymentRecord
        String status = params.get("status");
        constraintPaymentRecord.setStatus(status);
        constraintPaymentRecordService.updateById(constraintPaymentRecord);
        return "SUCCESS";
    }

}




