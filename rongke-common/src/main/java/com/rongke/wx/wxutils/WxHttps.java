package com.rongke.wx.wxutils;


import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * <dl>
 * <dd>描述：
 * <dl>
 *
 * @author 李巍
 * @date 2014-12-29
 * @time 下午03:46:11
 */
public class WxHttps {

    /**
     * https协议的post数据交互
     *
     * @param httpsUrl 请求的url
     * @param xmlStr   发送到 xml格式数据(根据微信基础应用访问数据格式订制)
     * @return
     */
    public static String post(String httpsUrl, String xmlStr) {
        HttpsURLConnection urlCon = null;
        try {
            urlCon = (HttpsURLConnection) (new URL(httpsUrl)).openConnection();
            urlCon.setDoInput(true);
            urlCon.setDoOutput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setUseCaches(false);
            if (null != xmlStr) {
                urlCon.setRequestProperty("Content-Length", String.valueOf(xmlStr.getBytes().length));
                urlCon.getOutputStream().write(xmlStr.getBytes("gbk"));
                urlCon.getOutputStream().flush();
                urlCon.getOutputStream().close();
            }
            //设置为gbk可以解决服务器接收时读取的数据中文乱码问题
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String line = null;
            String result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }

            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取基础应用票据access_token
     *
     * @return
     * @throws Exception
     */
    public String getBaseAccessToken() throws Exception {
        String appid = ConfKit.get("AppId");
        String secret = ConfKit.get("AppSecret");
        String jsonStr = WxHttps.post("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret, null);
        JSONObject obj = JSONObject.parseObject(jsonStr);
        if (obj.get("errcode") != null) {
            throw new Exception(obj.getString("errmsg"));
        }
        return obj.get("access_token").toString();
    }


    public static void main1(String[] args) throws Exception {
        String acc = new WxHttps().getBaseAccessToken();
        System.out.println(acc);
    }

}
