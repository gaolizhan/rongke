package com.rongke.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.model.ConstraintPaymentRecord;
import com.rongke.model.LoanOrder;
import com.rongke.model.UserBank;
import com.rongke.redis.CacheUtil;
import com.rongke.service.ConstraintPaymentRecordService;
import com.rongke.service.LoanOrderService;
import com.rongke.service.PersonRecordService;
import com.rongke.service.UserBankService;
import com.rongke.service.UserService;
import com.rongke.web.payutil.helipay.action.QuickPayApi;
import com.rongke.web.payutil.helipay.util.MessageHandle;
import com.rongke.web.payutil.helipay.vo.request.BindCardPayVo;
import com.rongke.web.service.ConstraintPaymentService;
import com.rongke.web.yibaopay.YbParamConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ConstraintPaymentServiceImpl implements ConstraintPaymentService {
    @Autowired
    private PersonRecordService personRecordService;
    @Autowired
    private UserService userService;
    @Autowired
    private YbParamConfig ybParamConfig;

    @Autowired
    private LoanOrderService loanOrderService;

    @Autowired
    private UserBankService userBankService;
    @Autowired
    private ConstraintPaymentRecordService constraintPaymentRecordService;
    @Autowired
    private CacheUtil cacheUtil;


    @Override
    @Transactional
    public String payment(Long loanId, String peyment) throws Exception {
        LoanOrder loanOrder = loanOrderService.selectById(loanId);

        EntityWrapper<UserBank> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_id", loanOrder.getUserId());
        UserBank userBank = userBankService.selectOne(entityWrapper);
        BindCardPayVo bindCardPayVo = new BindCardPayVo();
        bindCardPayVo.setP3_bindId(userBank.getBindId());
        bindCardPayVo.setP4_userId(userBank.getUserId()+"");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String  time  = format.format(new Date());
        String  orderId =  loanId+time;
        bindCardPayVo.setP5_orderId(orderId);
        bindCardPayVo.setP6_timestamp(time);
        bindCardPayVo.setP7_currency("CNY");
        bindCardPayVo.setP8_orderAmount(peyment);
        bindCardPayVo.setP9_goodsName("绑卡支付");
        bindCardPayVo.setP11_terminalType("MAC");
        bindCardPayVo.setP12_terminalId(MessageHandle.getMACAddress());
        bindCardPayVo.setP13_orderIp("127.0.0.1");
        bindCardPayVo.setP16_serverCallbackUrl(QuickPayApi.HELIPAY_NOTIFY_URL);
        JSONObject result  = QuickPayApi.bindCardPay(bindCardPayVo);
        if("0000".equals(result.getString("rt2_retCode"))){
            String orderStatus = result.getString("rt9_orderStatus");
            if("INIT".equals(orderStatus)||"SUCCESS".equals(orderStatus)||"DOING".equals(orderStatus)) {
                cacheUtil.set(orderId, orderId + "," + peyment + ",1" , 120 * 120);
                loanOrder.setLianRepayNum(orderId);
                loanOrderService.insertOrUpdate(loanOrder);
                ConstraintPaymentRecord constraintPaymentRecord = new ConstraintPaymentRecord();
                constraintPaymentRecord.setStatus(result.getString("rt9_orderStatus"));
                constraintPaymentRecord.setIdCard(userBank.getIdcardno());
                constraintPaymentRecord.setName(userBank.getName());
                constraintPaymentRecord.setPhone(userBank.getBankPhone());
                constraintPaymentRecord.setBankCardNo(userBank.getBankcardno());
                constraintPaymentRecord.setCreateTime(new Date());
                constraintPaymentRecord.setAmount(new BigDecimal(peyment));

                constraintPaymentRecord.setOrderId(loanOrder.getId());
                constraintPaymentRecord.setUserId(loanOrder.getUserId());
                constraintPaymentRecord.setRequestno(orderId);
                constraintPaymentRecordService.insert(constraintPaymentRecord);

                return "success";
            }
        }else if (result.get("rt3_retMsg") != null) {
            return result.getString("rt3_retMsg");
        }
        return "支付异常!";

//
//        Map<String, String> resultMap = YbApiHandler.constraintPayment(userBank.getBankPhone(), userBank.getBankcardno(), peyment,loanId);
//        return processorData(resultMap,loanOrder,userBank,peyment);
    }

//    public String processorData(Map<String, String> resultMap, LoanOrder loanOrder, UserBank userBank,String payment) throws ParseException {
//        String result;
//        if(!"PROCESSING".equals(resultMap.get("status").trim())){
//            result=resultMap.get("errormsg");
//        }else{
//            cacheUtil.set(resultMap.get("requestno").toString(), loanOrder.getId() + "," + payment + "," + 1, 120 * 120);
//            //更新状态
//            loanOrder.setLianRepayNum(resultMap.get("requestno").toString());
//            loanOrderService.insertOrUpdate(loanOrder);
//
//            //保存该条记录
//            ConstraintPaymentRecord constraintPaymentRecord = new ConstraintPaymentRecord();
//            constraintPaymentRecord.setStatus(resultMap.get("status"));
//            constraintPaymentRecord.setIdCard(userBank.getIdcardno());
//            constraintPaymentRecord.setName(userBank.getName());
//            constraintPaymentRecord.setPhone(userBank.getBankPhone());
//            constraintPaymentRecord.setBankCardNo(userBank.getBankcardno());
//            constraintPaymentRecord.setCreateTime(new Date());
//            constraintPaymentRecord.setAmount(new BigDecimal(payment));
//
//            constraintPaymentRecord.setOrderId(loanOrder.getId());
//            constraintPaymentRecord.setUserId(loanOrder.getUserId());
//            constraintPaymentRecord.setRequestno(resultMap.get("requestno").toString());
//            constraintPaymentRecordService.insert(constraintPaymentRecord);
//
//            result="success";
//        }
//        //请求出现异常,抛出异常信息
//        return result;
//    }

    private static String getMACAddress() throws Exception {
        InetAddress ia = InetAddress.getLocalHost();
        // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

        // 下面代码是把mac地址拼装成String
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            // mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }

        // 把字符串所有小写字母改为大写成为正规的mac地址并返回
        return sb.toString().toUpperCase().replaceAll("-", "");
    }
}
