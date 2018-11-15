package com.rongke.web.lianpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lianpay.api.util.TraderRSAUtil;
import com.rongke.web.lianpay.bean.BankCardAgreeBean;
import com.rongke.web.lianpay.bean.BankCardPayBean;
import com.rongke.web.lianpay.constant.PaymentConstant;
import com.rongke.web.lianpay.security.Md5Algorithm;
import com.rongke.web.lianpay.util.YTHttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * SDK SERVER testcase
 *
 * @author guoyx
 * @version :1.0
 * @date:May 13, 2013 5:09:31 PM
 */
public class FenqiPayment {
    private static final Logger logger = LoggerFactory.getLogger(PaymentApi.class);

    private static String TRADER_MD5_KEY = "201608101001022519_test_20160810";

    /*
     * 授权
     */
    public static String agreenoauthapply(String no_agree, String repayment_no, String money, String time, String userId) {
        BankCardAgreeBean bankCardAgreeBean = new BankCardAgreeBean();
        bankCardAgreeBean.setOid_partner(PaymentConstant.OID_PARTNER);//商户号
        //商户用户id
        bankCardAgreeBean.setUser_id(userId);
        bankCardAgreeBean.setSign_type("RSA");
        bankCardAgreeBean.setApi_version("1.0");
        bankCardAgreeBean.setRepayment_no(repayment_no);
        //分期计划时间需要传当天或者之后的日期
        bankCardAgreeBean.setRepayment_plan("{\"repaymentPlan\":[{\"date\":\"" + time + "\",\"amount\":\"" + money + "\"}]}");
        //短信参数字段, contract_type 商户名称 , contact_way 商户联系电话
        bankCardAgreeBean.setSms_param("{\"contract_type\":\"商奇宝\",\"contact_way\":\"123456\"}");
        bankCardAgreeBean.setPay_type("D");
        //调用签约接口后返回的协议号,和 用户id 对应
        bankCardAgreeBean.setNo_agree(no_agree);
        bankCardAgreeBean.setSign(genSign(JSON.parseObject(JSON.toJSONString(bankCardAgreeBean))));
        String reqJson = JSON.toJSONString(bankCardAgreeBean);
        //HttpRequestSimple httpclent = new HttpRequestSimple();
        //String resJson = httpclent.postSendHttp(SERVER,reqJson);
        String resJson = YTHttpHandler.getInstance().doRequestPostString(reqJson, "https://repaymentapi.lianlianpay.com/agreenoauthapply.htm");
        logger.info("结果报文为:" + resJson);
        return resJson;
    }

    /*
     * 修改分期计划
     */
    public static String updatePayPlan(String repayment_no, String money, String time, String userId) {
        BankCardAgreeBean bankCardAgreeBean = new BankCardAgreeBean();
        bankCardAgreeBean.setOid_partner(PaymentConstant.OID_PARTNER);//商户号
        //商户用户id
        bankCardAgreeBean.setUser_id(userId);
        bankCardAgreeBean.setSign_type("RSA");
        bankCardAgreeBean.setApi_version("1.0");
        bankCardAgreeBean.setRepayment_no(repayment_no);
        //分期计划时间需要传当天或者之后的日期
        bankCardAgreeBean.setRepayment_plan("{\"repaymentPlan\":[{\"date\":\"" + time + "\",\"amount\":\"" + money + "\"}]}");
        //短信参数字段, contract_type 商户名称 , contact_way 商户联系电话
        bankCardAgreeBean.setSms_param("{\"contract_type\":\"融亿借\",\"contact_way\":\"057187381988\"}");
        bankCardAgreeBean.setPay_type("D");
        //调用签约接口后返回的协议号,和 用户id 对应
        bankCardAgreeBean.setSign(genSign(JSON.parseObject(JSON.toJSONString(bankCardAgreeBean))));
        String reqJson = JSON.toJSONString(bankCardAgreeBean);
        //HttpRequestSimple httpclent = new HttpRequestSimple();
        //String resJson = httpclent.postSendHttp(SERVER,reqJson);
        String resJson = YTHttpHandler.getInstance().doRequestPostString(reqJson, "https://repaymentapi.lianlianpay.com/repaymentplanchange.htm");
        logger.info("结果报文为:" + resJson);
        return resJson;
    }

    /*
     * 还款
     */
    public static void bankcardrepayment(String userId, String no_order, String repayment_no, String money, String time, String no_agree, String orderId, String registerTime, String userPhone, String fullName, String idNo) {
        BankCardPayBean bankCardPayBean = new BankCardPayBean();
        bankCardPayBean.setOid_partner(PaymentConstant.OID_PARTNER);//商户号
        bankCardPayBean.setBusi_partner("101001");//交易类型
        bankCardPayBean.setUser_id(userId);//用户唯一标识
        bankCardPayBean.setSign_type("RSA");
        bankCardPayBean.setApi_version("1.0");
        bankCardPayBean.setNo_order(no_order);//订单号
        bankCardPayBean.setDt_order(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString());//商户订单时间
        bankCardPayBean.setRepayment_no(repayment_no);//还款计划编号
        bankCardPayBean.setName_goods("还款");
        bankCardPayBean.setInfo_order("还款");
        bankCardPayBean.setMoney_order(money);//还款金额
        bankCardPayBean.setInfo_order("还款");
        bankCardPayBean.setSchedule_repayment_date(time);//计划还款日期
        String condition = "{\"frms_ware_category\":\"2013\",\"user_info_mercht_userno\":\"" + userId + "\",\"user_info_dt_register\":\"" + registerTime + "\",\"user_info_bind_phone\":\"" + userPhone + "\",\"user_info_identify_state\":\"1\",\"user_info_identify_type\":\"1\",\"user_info_full_name\":\"" + fullName + "\",\"user_info_id_no\":\"" + idNo + "\"}";
        bankCardPayBean.setRisk_item(condition);//风控参数
        bankCardPayBean.setNotify_url("http://118.31.42.175/api/lianpay/fenqipayapi2/receiveNotify.htm?orderId=" + orderId);//回调地址
        bankCardPayBean.setPay_type("D");
        bankCardPayBean.setNo_agree(no_agree);//签约协议号
        bankCardPayBean.setSign(genSign(JSON.parseObject(JSON.toJSONString(bankCardPayBean))));
        String reqJson = JSON.toJSONString(bankCardPayBean);

        String resJson = YTHttpHandler.getInstance().doRequestPostString(reqJson, "https://repaymentapi.lianlianpay.com/bankcardrepayment.htm");
        logger.info("结果报文为:" + resJson);
    }

    private static String genSign(JSONObject reqObj) {
        String sign = reqObj.getString("sign");
        String sign_type = reqObj.getString("sign_type");
        // // 生成待签名串
        String sign_src = genSignData(reqObj);
        System.out.println("商户[" + reqObj.getString("oid_partner") + "]待签名原串"
                + sign_src);
        System.out.println("商户[" + reqObj.getString("oid_partner") + "]签名串"
                + sign);

        if ("MD5".equals(sign_type)) {
            sign_src += "&key=" + TRADER_MD5_KEY;
            return signMD5(sign_src);
        }
        if ("RSA".equals(sign_type)) {
            return getSignRSA(sign_src);
        }
        return null;
    }

    private static String signMD5(String signSrc) {

        try {
            return Md5Algorithm.getInstance().md5Digest(
                    signSrc.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * RSA签名验证
     *
     * @param
     * @return
     */
    public static String getSignRSA(String sign_src) {
        return TraderRSAUtil.sign(PaymentConstant.BUSINESS_PRIVATE_KEY, sign_src);
    }

    /**
     * 生成待签名串
     *
     * @param
     * @return
     */
    public static String genSignData(JSONObject jsonObject) {
        StringBuffer content = new StringBuffer();

        // 按照key做首字母升序排列
        List<String> keys = new ArrayList<String>(jsonObject.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            // sign 和ip_client 不参与签名
            if ("sign".equals(key)) {
                continue;
            }
            String value = (String) jsonObject.getString(key);
            // 空串不参与签名
            if (null == value) {
                continue;
            }
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        String signSrc = content.toString();
        if (signSrc.startsWith("&")) {
            signSrc = signSrc.replaceFirst("&", "");
        }
        return signSrc;
    }
}
