package com.rongke.wx.wxutils;


import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WeixinApiTicket {

    private static long lastUpdated;
    private static String ticket;

    private static final String TICKET_URI = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";


    public static String getRecentTicket(String token) throws Exception {
        if (ticket == null) {
            retrieveTicket(token);
        } else { //判断是否快要过期
            if ((System.currentTimeMillis() - lastUpdated) > 3600 * 1000) {
                retrieveTicket(token);
            }
        }
        return ticket;
    }

    /**
     * 获取ticket
     *
     * @throws Exception
     */
    private static void retrieveTicket(String token) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", token);
        params.put("type", "jsapi");
        JSONObject obj = JSONObject.parseObject(HttpKit.get(TICKET_URI, params));
        if (!obj.get("errmsg").equals("ok")) {
            throw new Exception(obj.getString("errmsg"));
        }
        lastUpdated = System.currentTimeMillis();
        ticket = obj.get("ticket").toString();
    }


}
