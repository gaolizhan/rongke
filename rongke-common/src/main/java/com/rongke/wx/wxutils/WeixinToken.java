package com.rongke.wx.wxutils;


import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

public class WeixinToken {

    private static long lastUpdated;
    private static String token;

    private static final String TOKEN_URI = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String REFRESH_TOKEN_URI = "https://api.weixin.qq.com/sns/oauth2/refresh_token";


    public static String getRecentToken() throws Exception {
        if (token == null) {
            retrieveToken();
        } else { //判断是否快要过期
            if ((System.currentTimeMillis() - lastUpdated) > 3600 * 1000) {
                retrieveToken();
            }
        }
        return token;
    }

    /**
     * 获取token
     *
     * @throws Exception
     */
    private static void retrieveToken() throws Exception {

        String appid = ConfKit.get("AppId");
        String secret = ConfKit.get("AppSecret");

        String jsonStr = WxHttps.post("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret, null);
        JSONObject obj = JSONObject.parseObject(jsonStr);
        if (obj.get("errcode") != null) {
            throw new Exception(obj.getString("errmsg"));
        }
        lastUpdated = System.currentTimeMillis();
        token = obj.get("access_token").toString();
        System.out.println("token=" + token);
    }

    /**
     * 通过code 换取 access_token
     *
     * @param code
     * @return
     * @throws IOException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static String exchangeToken(String code) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", ConfKit.get("AppId"));
        params.put("secret", ConfKit.get("AppSecret"));
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        return HttpKit.get(TOKEN_URI, params);
    }

    /**
     * 刷新 access_token
     *
     * @param refreshToken
     * @return
     * @throws IOException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static String getRefreshToken(String refreshToken) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", ConfKit.get("AppId"));
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", refreshToken);
        return HttpKit.get(REFRESH_TOKEN_URI, params);
    }

    /**
     * 获取微信用户id
     *
     * @param wxcode
     * @return
     * @throws Exception
     */
    public static String getWeChatId(String wxcode) throws Exception {
        JSONObject obj = JSONObject.parseObject(exchangeToken(wxcode));
        if (obj.get("errcode") != null) {
            throw new Exception(obj.getString("errmsg"));
        }
        String open_id = obj.get("openid").toString();
        return open_id;
    }

	/*public static void main(String[] args) throws Exception{
		System.out.println(getRecentToken()); 
		
	}*/
}
