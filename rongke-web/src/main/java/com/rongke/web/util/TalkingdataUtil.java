package com.rongke.web.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rongke.redis.CacheUtil;
import com.rongke.utils.JSONUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class TalkingdataUtil {

    private final static String url ="https://api.talkingdata.com/metrics/app/v1";

    private Logger log = Logger.getLogger(this.getClass());

    private final static String channelListUrl ="https://api.talkingdata.com/metrics/app/v1/channellist";

    private final static String accesskey ="";

    //查询的指标项
    private final static String[] metrics ={"activeuser"};

    private final static String groupby ="channelid";

    private  final static String talkingDataInfoKey = "yrhTalkingDataInfoKey";

    private final static  String channelMapKey = "yrhTalkingChannelMapKey";

    private static final CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

    @Autowired(required = false)
    public CacheUtil cacheUtil;

    static {
        HashMap<String, Object> talkDataParam = new HashMap<>();
        talkDataParam.put("accesskey",accesskey);
        talkDataParam.put("metrics",metrics);
        talkDataParam.put("groupby",groupby);
        HashMap<String,Object> filterParamMap = new HashMap<String,Object>();
        filterParamMap.put("start","2018-08-23");

        filterParamMap.put("end","2100-08-31");
        talkDataParam.put("filter",filterParamMap);
        talkDataParamJson = JSONUtils.beanToJson(talkDataParam);
    }
    private static String talkDataParamJson;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;//redis操作模板

    public JSONArray getTalkingdataInfo() throws Exception {
        Map<String, Object> channelMap = (Map<String, Object>)redisTemplate.opsForValue().get(channelMapKey);

        log.info("channelMap 等于空？======================="+channelMap == null);

        if(channelMap == null){
            JSONObject channelListResponse = sendPost(channelListUrl);
            log.info("商户结果：==========================" + JSON.toJSONString(channelListResponse, true));
            if(!"200".equals(channelListResponse.getString("status").trim())){
                return null;
            }else{
                JSONArray channelListResult = channelListResponse.getJSONArray("result");
                channelMap = new HashMap<>();
                for(int j=0;j<channelListResult.size();j++) {
                    log.info("获取商户结果数据开始============================");
                    String channelid = channelListResult.getJSONObject(j).getString("channelid");
                    String channelname = channelListResult.getJSONObject(j).getString("channelname");
                    log.info("商户channelid= " + channelid+"商户channelname"+channelname);
                    channelMap.put(channelid, channelname);
                }
                redisTemplate.opsForValue().set(channelMapKey, channelMap,60*60*24,TimeUnit.SECONDS);
                Thread.sleep(12000);
            }
        }else{
            for (String key : channelMap.keySet()) {
                log.info("渠道信息：key ="+key+"   value="+channelMap.get(key)+"==========================");
            }
        }
        JSONArray talkingDataInfoResult = (JSONArray)redisTemplate.opsForValue().get(talkingDataInfoKey);
        if(talkingDataInfoResult == null){
            JSONObject response = sendPost(url);
            log.info("数据结果：++++++++++++++++++++++++++++++++++" + JSON.toJSONString(response, true));
            if(!"200".equals(response.getString("status").trim())){
                return null;
            }else{
                talkingDataInfoResult = response.getJSONArray("result");
                redisTemplate.opsForValue().set(talkingDataInfoKey, talkingDataInfoResult,60*30,TimeUnit.SECONDS);
            }
        }

        JSONArray resultArray = new JSONArray();
        for (int j = 0; j < talkingDataInfoResult.size(); j++) {
            String channelid = (String) talkingDataInfoResult.getJSONObject(j).getString("channelid");
            if(channelMap.get(channelid) != null){
                String channelname = channelMap.get(channelid).toString();
                String activeuser = talkingDataInfoResult.getJSONObject(j).getString("activeuser");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("channelname", channelname);
                jsonObject.put("activeuser",activeuser);
                resultArray.add(jsonObject);
            }
        }
        return resultArray;
    }

    public static JSONObject sendPost(String url) throws IOException {
        StringEntity stringEntity = new StringEntity(talkDataParamJson, "UTF-8");
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Connection", "close");
        httpPost.setEntity(stringEntity);
        HttpResponse resp = closeableHttpClient.execute(httpPost);
        HttpEntity he = resp.getEntity();
        String respContent = EntityUtils.toString(he, "UTF-8");
        JSONObject response = (JSONObject) JSONObject.parse(respContent);
        return response;
    }
}
