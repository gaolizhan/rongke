package com.rongke.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.rongke.commons.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by bilibili on 2017/8/14.
 */
@Component
public class CacheUtilImpl implements CacheUtil {

    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;//redis操作模板

    public void put(String key, String value) {
        if (key == null || "".equals(key)) {
            return;
        }
        stringRedisTemplate.opsForHash().put(key, key, value);
    }

    public void put(String key, Object value) {
        if (key == null || "".equals(key)) {
            return;
        }
        stringRedisTemplate.opsForHash().put(key, key, new Gson().toJson(value));
    }

    public <T> T get(String key, Class<T> className) {
        Object obj = stringRedisTemplate.opsForHash().get(key, key);
        if (obj == null) {
            return null;
        }
        return new Gson().fromJson("" + obj, className);
    }

    public String get(String key) {
        return stringRedisTemplate.boundValueOps(key).get();
    }

    public void sendString(String key, Integer time) {
        try {
            stringRedisTemplate.boundValueOps(key).set(key, time, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getString(String key) {
        String temp = null;
        try {
            temp = (String) stringRedisTemplate.opsForHash().get(key, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return temp;
    }

    /**
     * 取得缓存（字符串类型）
     *
     * @param key
     * @return
     */
    public String getStr(String key, boolean retain) {
        String value = stringRedisTemplate.boundValueOps(key).get();
        if (!retain) {
            stringRedisTemplate.delete(key);
        }
        return value;
    }

    public boolean hasKey(String key) {
        if (key == null) {
            return false;
        }
        return stringRedisTemplate.hasKey(key);
    }

    public void delkey(String key) {

        stringRedisTemplate.delete(key);
    }

    /**
     * 将value对象写入缓存
     *
     * @param key
     * @param value
     * @param timeout 失效时间(秒)
     */
    public void set(String key, Object value, final long timeout) {
        if (value.getClass().equals(String.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Integer.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Double.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Float.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Short.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Long.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Boolean.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }
        if (timeout > 0) {
//            stringRedisTemplate.boundValueOps(key).set(key, time, TimeUnit.SECONDS);
            stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 将value对象以JSON格式写入缓存
     *
     * @param key
     * @param value
     * @param time  失效时间(秒)
     */
    public void setJson(String key, Object value, Integer time) {
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));
        if (time > 0) {
            stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 更新key对象field的值
     *
     * @param key   缓存key
     * @param field 缓存对象field
     * @param value 缓存对象field值
     */
    public void setJsonField(String key, String field, String value) {
        JSONObject obj = JSON.parseObject(stringRedisTemplate.boundValueOps(key).get());
        obj.put(field, value);
        stringRedisTemplate.opsForValue().set(key, obj.toJSONString());
    }

    /**
     * @param key   缓存key
     * @param value 缓存对象set 集合
     */
    public void addSet(String key, String... value) {
        stringRedisTemplate.boundSetOps(key).add(value);
    }

    @Override
    public void removeFromSet(String key, String... value) {
        stringRedisTemplate.boundSetOps(key).remove(value);
    }

    /**
     * @param key 缓存key
     */
    public Set getSet(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    @Override
    public void leftPush(String key, String value) {
        this.stringRedisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public String rightPop(String key) {
        return this.stringRedisTemplate.opsForList().rightPop(key);
    }
}
