package com.rongke.web.apix;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NameAndBankAuth {

    private static final Log log = LogFactory.getLog(NameAndBankAuth.class);
    private static final String apiUrl = "https://api.tongdun.cn/bodyguard/apply/v4";
    private static final String PARTNER_CODE = "nbsq";// 合作方标识
    private static final String PARTNER_KEY  = "";//合作方密钥
    private static final String PARTNER_APP  = "nbsq_and";//应用名
    private static HttpURLConnection conn;

    public static JSONObject invoke(Map<String, Object> params) {
        try {
            String urlString = new StringBuilder().append(apiUrl).append("?partner_code=").append(PARTNER_CODE).append("&partner_key=").append(PARTNER_KEY).append("&app_name=").append(PARTNER_APP).toString();
            URL url = new URL(urlString);
            // 组织请求参数
            StringBuilder postBody = new StringBuilder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() == null) continue;
                postBody.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(),
                        "utf-8")).append("&");
            }

            if (!params.isEmpty()) {
                postBody.deleteCharAt(postBody.length() - 1);
            }

            conn = (HttpURLConnection) url.openConnection();
            // 设置长链接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置连接超时
            conn.setConnectTimeout(1000);
            // 设置读取超时
            conn.setReadTimeout(3000);
            // 提交参数
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.getOutputStream().write(postBody.toString().getBytes());
            conn.getOutputStream().flush();
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                log.info("[BodyGuardApiInvoker] invoke failed, response status:" + responseCode);
                return null;
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
            return JSON.parseObject(result.toString());
        } catch (Exception e) {
            log.error("[BodyGuardApiInvoker] invoke throw exception, details: " + e);
        }
        return null;
    }

    //贷前审核
    public static JSONObject TongDunXDBB (String body,String app_name){
        String url = "https://api.tongdun.cn/bodyguard/apply/v4?partner_code=dxjk&partner_key=9c502ae5a41549f1a6ab74b58d6ba96b";
        url += "&app_name="+app_name;
        String result = HttpClientUtil.sendPostSSLRequest(url,body,"UTF-8","application/x-www-form-urlencoded");
        return JSON.parseObject(result);
    }

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id_number", "341024199409289737");
        params.put("account_name", "汪航");
        params.put("card_number", "6217001720005363946");
        params.put("account_mobile", "15257119142");
        String stringBuilder = new NameAndBankAuth().invoke(params).toString();
        System.out.println(stringBuilder);
    }
}
