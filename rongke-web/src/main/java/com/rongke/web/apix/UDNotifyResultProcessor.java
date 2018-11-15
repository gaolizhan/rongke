package com.rongke.web.apix;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongke.utils.MD5Utils;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bin on 2017/11/13.
 */
public class UDNotifyResultProcessor {

    /**
     * TODO 获取商户开户的PUB_KEY
     */
    public static final String PUB_KEY = "59f80015-0b56-4e7b-971f-2c634cacb4d0";

    /**
     * TODO 获取商户开户的 security_key
     */
    public static final String SECURITY_KEY = "9a7c79a3-3d6e-47f7-a5c6-237716175700";
    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final boolean IS_DEBUG = true;

    //商品编号
    static final String product_code = "Y1001005";

    static final String product_url = "https://api4.udcredit.com";

    private static String fformatStr = "/dsp-front/4.1/dsp-front/default/pubkey/%s/product_code/%s/out_order_id/%s/signature/%s";

    /**
     * @param pub_key 商户公钥
     * @param partner_order_id 商户订单号
     * @param sign_time 签名时间
     * @param security_key 商户私钥
     */

    public static String getMD5Sign (String pub_key, String partner_order_id, String sign_time,
                                     String security_key) {
        String signStr = String.format("pub_key=%s|partner_order_id=%s|sign_time=%s|security_key=%s", pub_key, partner_order_id, sign_time, security_key);
        System.out.println("测试输入签名 signField："+ signStr);
        return MD5Utils.MD5Encrpytion(signStr);
    }

    /**
     * 接收实名认证异步通知
     */
    public void process(HttpServletRequest request, HttpServletResponse response) throws
            IOException {
        final JSONObject reqObject = getRequestJson(request);
        JSONObject respJson = new JSONObject();
        //验签
        String sign = reqObject.getString("sign");
        String sign_time = reqObject.getString("sign_time");
        String partner_order_id = reqObject.getString("partner_order_id");
        System.out.println("sign：" + sign);
        String signMD5 = getMD5Sign(PUB_KEY, partner_order_id, sign_time, SECURITY_KEY);
        System.out.println("signMD5：" + signMD5);
        if (!sign.equals(signMD5)) {
            System.err.println("异步通知签名错误");
            respJson.put("code", "0");
            respJson.put("message", "签名错误");
        } else {
            System.out.print("收到商户异步通知");
            respJson.put("code", "1");
            respJson.put("message", "收到通知");
            //TODO 异步执行商户自己的业务逻辑(以免阻塞返回导致通知多次)
            Thread asyncThread = new Thread("asyncDataHandlerThread") {
                public void run() {
                    System.out.println("异步执行商户自己的业务逻辑...");
                    try {
                        String id_name = reqObject.getString("id_name");
                        String id_number = reqObject.getString("id_number");
                        System.out.println(id_name + "：" + id_number);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            asyncThread.start();
        }
        System.out.println("返回结果：" + respJson.toJSONString());
        //返回结果
        response.setCharacterEncoding(CHARSET_UTF_8);
        response.setContentType("application/json; charset=utf-8");
        response.getOutputStream().write(respJson.toJSONString().getBytes(CHARSET_UTF_8));
    }
    /**
     * 获取请求 json 对象
     */
    public static JSONObject getRequestJson(HttpServletRequest request) throws IOException {
        InputStream in = request.getInputStream();
        byte[] b = new byte[10240];
        int len;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = in.read(b)) > 0) {
            baos.write(b, 0, len);
        }
        String bodyText = new String(baos.toByteArray(), CHARSET_UTF_8);
        JSONObject json = (JSONObject) JSONObject.parse(bodyText);
        if (IS_DEBUG) {
            System.out.println("received notify message:");
            System.out.println(JSON.toJSONString(json, true));
        }
        return json;
    }

    /**
     * 人像对比
     */
    public static String apiCall(String idNo,String idName) throws Exception {
        if (idNo == null || idNo.isEmpty() || idName == null || idName.isEmpty())
            throw new Exception("error ! the parameter Map can't be null.");
        StringBuffer bodySb = new StringBuffer("{");
        Map<String, String> parameter = new HashMap<>();
        parameter.put("id_no",idNo);
        parameter.put("id_name",idName);
        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            bodySb.append("'").append(entry.getKey()).append("':'").append(entry.getValue()).
                    append("',");
        }
        String bodyStr = bodySb.substring(0, bodySb.length() - 1) + "}";
        String signature = md5(bodyStr + "|" + SECURITY_KEY);
        String url = product_url+String.format(fformatStr, PUB_KEY, product_code,
                System.currentTimeMillis() + "", signature);
        System.out.println("requestUrl=>" + url);
        System.out.println("request parameter body=>" + bodyStr);
        HttpResponse r = makePostRequest(url, bodyStr);
        return EntityUtils.toString(r.getEntity());
    }
    private static final CloseableHttpClient client = HttpClientBuilder.create().build();
    private static HttpResponse makePostRequest(String uri, String jsonData)
            throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(URIUtil.encodeQuery(uri, "utf-8"));
        httpPost.setEntity(new StringEntity(jsonData, "UTF-8"));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        return client.execute(httpPost);
    }
    private static String md5(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data.toString().getBytes());
        return bytesToHex(md.digest());
    }
    private static String bytesToHex(byte[] ch) {
        StringBuffer ret = new StringBuffer("");
        for (int i = 0; i < ch.length; i++)
            ret.append(byteToHex(ch[i]));
        return ret.toString();
    }
    /**
     * 字节转换为16进制字符串
     */
    private static String byteToHex(byte ch) {
        String str[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E",
                "F" };
        return str[ch >> 4 & 0xF] + str[ch & 0xF];
    }


    public static void main(String[] args) {
        try {
            String result = UDNotifyResultProcessor.apiCall("342522199410102111","熊磊");
            System.out.print(result);
            int a = 1;
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}


