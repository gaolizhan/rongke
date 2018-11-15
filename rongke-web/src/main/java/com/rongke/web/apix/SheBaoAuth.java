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
public class SheBaoAuth {
    private final static String APIX_KEY = "apix-key";

    private static String apixKey = "";

    public static JSONObject getCityAndType(String province_id) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        String reqUrl = "http://e.apix.cn/apixanalysis/shebao/cities";

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

    public static JSONObject getConfirm(String login_type,String unique_key,String params) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        String reqUrl = "http://e.apix.cn/apixanalysis/shebao/login";

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
        myJsonObject = myJsonObject.parseObject(EntityUtils.toString(response.getEntity()));
        return myJsonObject;
    }

/*    public static void main(String[] args) {
        String httpUrl = " http://v.apix.cn/apixcredit/idcheck/bankcard";
        String httpArg = "type=银行卡核验类型&bankcardno=银行卡号&name=姓名&idcardno=身份证号&phone=预留手机号";
        String jsonResult = request(httpUrl, httpArg);
        System.out.println(jsonResult);
    }*/
}
