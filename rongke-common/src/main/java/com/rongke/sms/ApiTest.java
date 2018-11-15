//package com.rongke.sms;
//
//import com.alibaba.fastjson.JSONObject;
//import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.NameValuePair;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.params.HttpMethodParams;
//
//import java.net.URLEncoder;
//
///**
// * Created by bilibili on 2017/11/23.
// */
//public class ApiTest {
//
//    //普通短信
//    private void sendsms() throws Exception {
//        HttpClient httpClient = new HttpClient();
//        PostMethod postMethod = new PostMethod("http://api.1cloudsp.com/api/v2/send");
//        postMethod.getParams().setContentCharset("UTF-8");
//        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
//
//        String accesskey = "gioo5xSFOdcIOjvK"; //用户开发key
//        String accessSecret = "P8QPRSTK4KOzidVofonmr8h4RpCVpedv"; //用户开发秘钥
//
//        NameValuePair[] data = {
//                new NameValuePair("accesskey", accesskey),
//                new NameValuePair("secret", accessSecret),
//                new NameValuePair("sign", "123"),
//                new NameValuePair("templateId", "100"),
//                new NameValuePair("mobile", "13900000001,13900000002"),
//                new NameValuePair("content", URLEncoder.encode("先生##9:40##快递公司##1234567", "utf-8"))//（示例模板：{1}您好，您的订单于{2}已通过{3}发货，运单号{4}）
//        };
//        postMethod.setRequestBody(data);
//        int statusCode = httpClient.executeMethod(postMethod);
//        System.out.println("statusCode: " + statusCode + ", body: "
//                + postMethod.getResponseBodyAsString());
//    }
//
//    //个性短信
//    private void sendsms2() throws Exception {
//        HttpClient httpClient = new HttpClient();
//        PostMethod postMethod = new PostMethod("http://api.1cloudsp.com/api/v2/send");
//        postMethod.getParams().setContentCharset("UTF-8");
//        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
//
//        String accesskey = "xxxxxxxxxx"; //用户开发key
//        String accessSecret = "yyyyyyyyyy"; //用户开发秘钥
//
//        //组装个性短信内容
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("13700000000","先生##9:40##快递公司##1234567");
//        jsonObj.put("13700000001","女士##10:10##物流公司##000000");//（示例模板：{1}您好，您的订单于{2}已通过{3}发货，运单号{4}）
//
//        NameValuePair[] data = {
//                new NameValuePair("accesskey", accesskey),
//                new NameValuePair("secret", accessSecret),
//                new NameValuePair("sign", "123"),
//                new NameValuePair("templateId", "100"),
//                new NameValuePair("data", URLEncoder.encode(jsonObj.toString(), "utf-8"))
//        };
//        postMethod.setRequestBody(data);
//
//        int statusCode = httpClient.executeMethod(postMethod);
//        System.out.println("statusCode: " + statusCode + ", body: "
//                + postMethod.getResponseBodyAsString());
//    }
//
//    public static void main(String[] args) throws Exception {
//        ApiTest t = new ApiTest();
////        //普通短信
//          t.sendsms();
//        //个性短信
////        t.sendsms2();
//    }
//
//}
//
