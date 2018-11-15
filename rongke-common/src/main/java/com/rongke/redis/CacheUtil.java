package com.rongke.redis;

import java.util.Set;

/**
 * Created by bilibili on 2017/8/14.
 */
public interface CacheUtil {

    /**
     * 向缓存中保存String类型数据
     */
    void put(String key, String value);

    /**
     * 向缓存中保存Object类型数据
     */
    void put(String key, Object value);

    /**
     * 向缓存中保存实体类型数据
     */
    <T> T get(String key, Class<T> className);

    /**
     * 从缓存中去数据  返回String类型
     */
    String get(String key);

    /**
     * 取值 （只使用于 put() 方法存值的取值方法）
     */
    String getString(String key);

    /**
     * @param key    键
     * @param retain 取出值后是否保留该键
     * @return
     */
    String getStr(String key, boolean retain);

    /**
     * 向缓存中保存String类型数据,time秒后失效
     */
    void sendString(String key, Integer time);

    /**
     * redis 缓存中是否存在key
     */
    boolean hasKey(String key);

    /**
     * 删除key
     */
    void delkey(String key);

    /**
     * @param key     键
     * @param value   值
     * @param timeout 失效时间 秒
     */
    void set(String key, Object value, final long timeout);

    /**
     * @param key   键
     * @param value json对象值
     * @param time  失效时间 秒
     */
    void setJson(String key, Object value, Integer time);

    /**
     * 更新key对象field的值
     *
     * @param key   缓存key
     * @param field 缓存对象field
     * @param value 缓存对象field值
     */
    void setJsonField(String key, String field, String value);

    /**
     * 向缓存中存放set集合
     *
     * @param key
     * @param value set集合
     */
    void addSet(String key, String... value);

    void removeFromSet(String key, String... value);

    /**
     * 从缓存总中获取set集合
     *
     * @param key
     * @return
     */
    Set getSet(String key);

    void leftPush(String key, String value);

    String rightPop(String key);
}
