package com.rongke.web.util;

import com.dahantc.api.sms.json.JSONHttpClient;
import org.apache.commons.httpclient.URIException;
import org.apache.log4j.Logger;

import java.util.UUID;

public class DhSmsUtil {
    private static final Logger LOG = Logger.getLogger(DhSmsUtil.class);
    private static String account = "";// 用户名（必填）
    private static String password = "";// 密码（必填）
    //    private static String phone = "18857627857"; // 手机号码（必填,多条以英文逗号隔开）
    public static String sign = "【易融花】"; // 短信签名（必填）
    public static String subcode = ""; // 子号码（可选）
    public static String msgid = UUID.randomUUID().toString().replace("-", ""); // 短信id，查询短信状态报告时需要，（可选）
    public static String sendtime = ""; // 定时发送时间（可选）

    public static void sendMessage(String content, String mobile) {

        JSONHttpClient jsonHttpClient = null;
        try {
            jsonHttpClient = new JSONHttpClient("http://www.dh3t.com");
        } catch (URIException e) {
            LOG.error("短信异常", e);
        }
        jsonHttpClient.setRetryCount(1);
        String sendhRes = jsonHttpClient.sendSms(account, password, mobile, content, sign, subcode);
        LOG.info("提交单条普通短信响应：" + sendhRes);
    }

//    public static void main(String[] args) {
//        try {
//            String content = "亲爱的用户，恭喜您，您的授信审核通过，可打开APP查看详情。";// 短信内容（必填）
//
//            JSONHttpClient jsonHttpClient = new JSONHttpClient("http://www.dh3t.com");
//            jsonHttpClient.setRetryCount(1);
//            String sendhRes = jsonHttpClient.sendSms(account, password, phone, content, sign, subcode);
//            LOG.info("提交单条普通短信响应：" + sendhRes);
//
//            //
//            // List<SmsData> list = new ArrayList<SmsData>();
//            // list.add(new
//            // SmsData("11111111,15711666133,1738786465,44554545",
//            // content, msgid, sign, subcode, sendtime));
//            // list.add(new SmsData("15711777134", content, msgid, sign,
//            // subcode, sendtime));
//            // String sendBatchRes = jsonHttpClient.sendBatchSms(account,
//            // password, list);
//            // LOG.info("提交批量短信响应：" + sendBatchRes);
//            //
//            // String reportRes = jsonHttpClient.getReport(account, password);
//            // LOG.info("获取状态报告响应：" + reportRes);
//            //
//            // String smsRes = jsonHttpClient.getSms(account, password);
//            // LOG.info("获取上行短信响应：" + smsRes);
//
//        } catch (Exception e) {
//            LOG.error("应用异常", e);
//        }
//    }
}