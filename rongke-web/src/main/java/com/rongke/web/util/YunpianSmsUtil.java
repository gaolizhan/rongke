package com.rongke.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 云片发送模板短信
 * @className: com.rongke.web.util
 * @date 2018/9/29. 7:16 PM
 */
public class YunpianSmsUtil {
    private static final String YP_REQ_TPL_SINGLE_URL = "https://sms.yunpian.com/v2/sms/tpl_single_send.json";
    private static final String YP_APIKEY = "8769866f3db5eb87e0d3a4d90f578bac";


    public static Boolean sendSms(YTempletEnum templet,String mobile,String[] arr){
        String tpl_value =null;
        if(arr != null) {
            tpl_value =  Arrays.stream(arr).map(str -> urlEncode("#code#", null) + "=" + urlEncode(str, null)).collect(Collectors.joining("&"));
        }
        try {
            Map<String,String> params = new HashMap<>();
            params.put("apikey", YP_APIKEY);
            params.put("mobile", mobile);
            params.put("tpl_id", templet.getCode());
            params.put("tpl_value",tpl_value);
            String result = HttpUtil.post(YP_REQ_TPL_SINGLE_URL,params);
            JSONObject json= JSON.parseObject(result);
            return json.getInteger("code")  == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static final String urlEncode(String text, String charset) {
        if (charset == null || "".equals(charset))
            charset = "UTF-8";
        try {
            return URLEncoder.encode(text, charset);
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return text;
    }


    public enum YTempletEnum {
        //短信模板枚举
        VERFIY("2561396", "验证码"),
        NOTIFY_THIRD_DAY("2561390", "还款日3天前发提醒通知短信"),
        NOTIFY_SECOND_DAY("2561392", "还款日2天前发提醒通知短信"),
        NOTIFY_NOW_DAY("2561402", "还款日当天前发提醒通知短信"),
        ORDER_EXAMINE_PASS("2561398", "订单审核通过发通知短信"),
        ORDER_SETTLED("2561400", "订单结清发通知短信"),
        ;
        private String code;
        private String desc;

        YTempletEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

}

