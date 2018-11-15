package com.rongke.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class YouDunUtil {

    private static Logger log = Logger.getLogger(YouDunUtil.class);

    static final String CHASET_UTF_8 = "UTF-8";
    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    /**
     * 接口调用地址
     */
    static final String dataservice_url = "https://api4.udcredit.com/dsp-front/4.1/dsp-front/default/pubkey/%s/product_code/%s/out_order_id/%s/signature/%s";

    /**
     * 商户pub_key
     */
    static final String pub_key = "59f80015-0b56-4e7b-971f-2c634cacb4d0";

    /**
     * 商户secretkey
     */
    static final String secretkey = "9a7c79a3-3d6e-47f7-a5c6-237716175700";

    /**
     * 产品编号：云慧眼用户画像：Y1001005
     * 请商户按照实际接入产品传入对应的产品编码
     */
    static final String product_code = "Y1001005";

    /**
     * 加签
     *
     * @param jsonObject
     * @param secretkey
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String getMd5(JSONObject jsonObject, String secretkey) throws Exception {
        String sign = String.format("%s|%s", jsonObject, secretkey);
        return MD5Encrpytion(sign.getBytes("UTF-8"));
    }

    public static JSONObject getUdInfoJson(JSONObject jsonObject,String udInfoId) throws Exception {
        String signature = getMd5(jsonObject, secretkey);
        String url = String.format(dataservice_url, pub_key, product_code, udInfoId, signature);
        log.info("url地址为：" + url);
        JSONObject response = doHttpRequest(url, jsonObject);
        log.info("输出结果：" + JSON.toJSONString(response, true));

        JSONObject header = response.getJSONObject("header");
        String ret_code = (String)header.get("ret_code");
        if (!"000000".equals(ret_code)) {
            log.error("有盾接口响应异常!!!响应码为:" + ret_code);
            throw new Exception("有盾数据查询失败，失败编码为:" + ret_code);
        }
        return response;
    }

    /**
     * MD5加密
     *
     * @param source
     * @return
     */
    private static final String MD5Encrpytion(byte[] source) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(source);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            for (int m = 0; m < str.length; ++m) {
                if ((str[m] >= 'a') && (str[m] <= 'z')) {
                    str[m] = (char) (str[m] - ' ');
                }
            }
            return new String(str);
        } catch (Exception e) {
        }
        return null;
    }



    private static final CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

    /**
     * http请求
     *
     * @param url
     * @param jsonObject
     * @return
     * @throws Exception
     */
    private static JSONObject doHttpRequest(String url, JSONObject jsonObject) throws Exception {
        //设置传入参数
        StringEntity stringEntity = new StringEntity(jsonObject.toJSONString(), CHASET_UTF_8);
        stringEntity.setContentEncoding(CHASET_UTF_8);
        stringEntity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Connection", "close");
        httpPost.setEntity(stringEntity);
        HttpResponse resp = closeableHttpClient.execute(httpPost);
        HttpEntity he = resp.getEntity();
        String respContent = EntityUtils.toString(he, CHASET_UTF_8);
        return (JSONObject) JSONObject.parse(respContent);
    }
}
