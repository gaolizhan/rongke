package com.rongke.redis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bilibili on 2017/8/14.
 */
public class ReidsTest {
    @Autowired
    private CacheUtil cacheUtil;
    //简单字符串处理

    public void testRedis(){
        //简单字符串处理
        cacheUtil.put("name", "test");
        System.out.println("String---name--"+cacheUtil.get("name"));

        //map
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("key", "value");
        map.put("key1", "value1");
        cacheUtil.put("map", map);
        //第一种取值方式
        Map map1 = cacheUtil.get("map",Map.class);
        if(map1 != null){
            System.out.println("first map---"+map1.get("key"));
        }
        //第二种取值方式
        Map map2 = new Gson().fromJson(cacheUtil.get("map"), new TypeToken<Map<String,Object>>() {}.getType());
        if(map2 != null){
            System.out.println("second map---"+map2.get("key1"));
        }

        //list<String>
        List<String> newlist = new ArrayList<String>();
        newlist.add("str1");
        newlist.add("sr2");
        cacheUtil.put("newlist", newlist);
        List<String> newlist1 =  new Gson().fromJson(cacheUtil.get("newlist"), new TypeToken<List<String>>(){}.getType());
        System.out.println("list<String>--"+newlist1);

        //List<Map<String,Object>>
        List<Map<String,Object>> nowlist = new ArrayList<Map<String,Object>>();
        Map<String,Object> newmap = new HashMap<String,Object>();
        newmap.put("key1", "value1");
        newmap.put("key2", "value2");
        nowlist.add(newmap);
        cacheUtil.put("nowlist", nowlist);
        List<Map<String,Object>> nowlist1 =  new Gson().fromJson(cacheUtil.get("nowlist"), new TypeToken<List<Map<String,Object>>>(){}.getType());
        if(nowlist1 !=null ){
            System.out.println(nowlist1.get(0).get("key1"));
        }
        System.out.println("List<Map<String,Object>>--"+nowlist1);

    }




}
