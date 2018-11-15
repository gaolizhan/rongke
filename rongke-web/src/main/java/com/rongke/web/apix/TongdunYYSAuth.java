package com.rongke.web.apix;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TongdunYYSAuth {
    private static String partnerCode = "";
    private static String partnerKey = "";

    /**
     * 同盾创建任务方法
     *
     * @param name
     * @param idcard
     * @param mobile
     * @return
     */
    public static JSONObject createTask(String name, String idcard, String mobile) {
        String url = "https://api.shujumohe.com/octopus/task.unify.create/v3?partner_code=" + partnerCode + "&partner_key=" + partnerKey + "";
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("channel_type", "YYS");
        bodyMap.put("channel_code", "100000");
        bodyMap.put("real_name", name);
        bodyMap.put("identity_code", idcard);
        bodyMap.put("user_mobile", mobile);
        String body = "channel_type=YYS&channel_code=100000&real_name=" + name + "&identity_code=" + idcard + "&user_mobile=" + mobile;
        String result = HttpClientUtil.sendPostSSLRequest(url, body, "UTF-8", "application/x-www-form-urlencoded");
        System.out.print(result);
        return JSON.parseObject(result);
    }

    /**
     * @param taskId
     * @param userName
     * @param userPass
     * @param taskStage
     * @param requestType
     * @param map
     * @return
     */
    public static JSONObject acquire(String taskId, String userName, String userPass, String taskStage, String requestType, Map<String, String> map) {
        String url = "https://api.shujumohe.com/octopus/yys.unify.acquire/v3?partner_code=" + partnerCode + "&partner_key=" + partnerKey + "";
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("task_id", taskId);
        bodyMap.put("user_name", userName);
        bodyMap.put("user_pass", userPass);
        bodyMap.put("task_stage", taskStage);
        bodyMap.put("request_type", requestType);
        String body = "task_id=" + taskId + "&user_name=" + userName + "&user_pass=" + userPass + "&task_stage=" + taskStage + "&request_type=" + requestType;
        if (map != null) {
            for (String key : map.keySet()) {
                String value = map.get(key);
                body += "&" + key + "=" + value;
            }
        }

        String result = HttpClientUtil.sendPostSSLRequest(url, body, "UTF-8", "application/x-www-form-urlencoded");
        System.out.print(result);
        return JSON.parseObject(result);
    }

    public static JSONObject getAll(String taskId) {
        String url = "https://api.shujumohe.com/octopus/task.unify.query/v3?partner_code=" + partnerCode + "&partner_key=" + partnerKey + "";
        String body = "task_id=" + taskId;
        String result = HttpClientUtil.sendPostSSLRequest(url, body, "UTF-8", "application/x-www-form-urlencoded");
        return JSON.parseObject(result);
    }

    public static JSONObject getYYSMoFangAll(String taskId) {
        String url = "https://api.shujumohe.com/octopus/report.task.query/v2?partner_code=" + partnerCode + "&partner_key=" + partnerKey + "";
        String body = "task_id=" + taskId;
        String result = HttpClientUtil.sendPostSSLRequest(url, body, "UTF-8", "application/x-www-form-urlencoded");
        return JSON.parseObject(result);
    }
}
