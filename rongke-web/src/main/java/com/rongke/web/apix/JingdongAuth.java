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
public class JingdongAuth {
    private final static String APIX_KEY = "apix-key";

    private static String apixKey = "";

    public static JSONObject getConfirm(String callback_url,String success_url,String failed_url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        String reqUrl = "http://e.apix.cn/apixanalysis/jd/grant/ele_business/jingdong/jd/page";

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

        String reqUrl = "http://e.apix.cn/apixanalysis/jd/retrieve/ele_business/jingdong/query";

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

    public static JSONObject getSafeCode(String login_name,String safe_code) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        String reqUrl = "http://e.apix.cn/apixanalysis/tb/grant/ele_business/taobao/safeCode/check";

        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader(APIX_KEY, apixKey)
                .addParameter("login_name", login_name)
                .addParameter("safe_code", safe_code)
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
