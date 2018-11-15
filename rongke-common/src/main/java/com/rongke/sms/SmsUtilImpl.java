//package com.rongke.sms;
//
//import com.rongke.redis.CacheUtil;
//import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.NameValuePair;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.params.HttpMethodParams;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.net.URLEncoder;
//
///**
// * Created by bilibili on 2017/8/30.
// */
//@Component
//public class SmsUtilImpl implements SmsUtil {
//    //官网地址：tr.1cloudsp.com
//    private final String accesskey = "sumIZ1d51KxeAJlF";//用户开发key
//    private final String accessSecret = "8mGtcLFiUv73BoeCUpXQmrIUYKHw8FKz";//用户开发秘钥
//    private final String sign = "【易融花】";//用户签名
//    @Resource
//    private CacheUtil cacheUtil;
//
//    //普通短信
//    public Boolean sendsms(String mobile, String content) throws Exception {
//        boolean sendsucce = false;
//        HttpClient httpClient = new HttpClient();
//        PostMethod postMethod = new PostMethod("http://api.1cloudsp.com/api/v2/single_send");
//        postMethod.getParams().setContentCharset("UTF-8");
//        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
//
//        NameValuePair[] data = {
//                new NameValuePair("accesskey", accesskey),
//                new NameValuePair("secret", accessSecret),
//                new NameValuePair("sign", sign),
//                new NameValuePair("templateId", "9068"),
//                new NameValuePair("mobile", mobile),
//                new NameValuePair("content", URLEncoder.encode(content, "utf-8"))//（示例模板：{1}您好，您的订单于{2}已通过{3}发货，运单号{4}）
//        };
//        postMethod.setRequestBody(data);
//
//        int statusCode = httpClient.executeMethod(postMethod);
//        System.out.println("statusCode: " + statusCode + ", body: "
//                + postMethod.getResponseBodyAsString());
//        if (statusCode == 200) {
//            sendsucce = true;
//        }
//        return sendsucce;
//    }
//
//    @Override
//    public Boolean checkCode(String phone, String code) {
//        Boolean check = false;
//        if (cacheUtil.hasKey(phone)) {
//            try {
//                String cachecode = cacheUtil.get(phone);
//                if (cachecode.equals(cachecode)) {
//                    check = true;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return check;
//    }
//
//   /* public static void main(String[] args) throws Exception {
//        sendsms();
//    }*/
//}
