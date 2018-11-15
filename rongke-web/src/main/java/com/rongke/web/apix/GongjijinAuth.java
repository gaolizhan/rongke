package com.rongke.web.apix;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

/**
 * Created by Administrator on 2017/8/2 0002.
 */
@CrossOrigin
public class GongjijinAuth {
    private final static String APIX_KEY = "apix-key";

    private static String apixKey = "";

    public static JSONObject getType(String province_id) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        String reqUrl = "http://e.apix.cn/apixanalysis/gjj/citys";

        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader(APIX_KEY, apixKey)
                .addParameter("province_id", province_id)
                .build();

        CloseableHttpResponse response = client.execute(request);

        //将字符串转换成jsonObject对象
        JSONObject myJsonObject = new JSONObject();
        myJsonObject=myJsonObject.parseObject( EntityUtils.toString(response.getEntity()));
        return myJsonObject;
    }

    public static JSONObject getCode(String login_type,String unique_key,String login_name) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        String reqUrl = "http://e.apix.cn/apixanalysis/gjj/capcha";

        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader(APIX_KEY, apixKey)
                .addParameter("login_type", login_type)
                .addParameter("unique_key", unique_key)
                .addParameter("login_name", login_name)
                .build();

        CloseableHttpResponse response = client.execute(request);

        //将字符串转换成jsonObject对象
        JSONObject myJsonObject = new JSONObject();
        myJsonObject=myJsonObject.parseObject( EntityUtils.toString(response.getEntity()));
        return myJsonObject;
    }

    public static JSONObject getConfirm(String login_type,String unique_key,String params) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        String reqUrl = "http://e.apix.cn/apixanalysis/gjj/login";

        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader(APIX_KEY, apixKey)
                .addParameter("login_type", login_type)
                .addParameter("unique_key", unique_key)
                .addParameter("params", params)
                .build();

        CloseableHttpResponse response = client.execute(request);

        //将字符串转换成jsonObject对象
        JSONObject myJsonObject = new JSONObject();
        myJsonObject=myJsonObject.parseObject( EntityUtils.toString(response.getEntity()));
        return myJsonObject;
    }

    public static JSONObject getH5PageUrl(String callback_url,String success_url,String failed_url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        String reqUrl = "http://e.apix.cn/apixanalysis/gjj/page";

        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader(APIX_KEY, apixKey)
                .addParameter("callback_url", callback_url)
                .addParameter("success_url", success_url)
                .addParameter("failed_url", failed_url)
                .build();

        CloseableHttpResponse response = client.execute(request);

        //将字符串转换成jsonObject对象
        JSONObject myJsonObject = new JSONObject();
        myJsonObject=myJsonObject.parseObject( EntityUtils.toString(response.getEntity()));
        return myJsonObject;
    }

    public static JSONObject getData(String query_code) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        String reqUrl = "http://e.apix.cn/apixanalysis/gjj/query";

        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader(APIX_KEY, apixKey)
                .addParameter("query_code", query_code)
                .build();

        CloseableHttpResponse response = client.execute(request);

        //将字符串转换成jsonObject对象
        JSONObject myJsonObject = new JSONObject();
        myJsonObject=myJsonObject.parseObject( EntityUtils.toString(response.getEntity()));
        return myJsonObject;
    }

/*    public static void main(String[] args) {
        String httpUrl = " http://v.apix.cn/apixcredit/idcheck/bankcard";
        String httpArg = "type=银行卡核验类型&bankcardno=银行卡号&name=姓名&idcardno=身份证号&phone=预留手机号";
        String jsonResult = request(httpUrl, httpArg);
        System.out.println(jsonResult);
    }*/
}
