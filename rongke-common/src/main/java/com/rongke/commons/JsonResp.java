package com.rongke.commons;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.utils.ArrayUtils;
import com.rongke.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;

/**
 * 接口返回JSON格式
 *
 * @param <T>
 */
public class JsonResp<T> implements Serializable {
    /**
     * 代码消息
     */
    private JsonCodeEnum code;
    /**
     * 数据
     */
    private T data;
    /**
     * 消息
     */
    private String msg;
    private boolean success = false;

    public boolean isSuccess() {
        return success;
    }

    public JsonResp(JsonCodeEnum code, String msg) {
        super();
        setCode(code);
        this.msg = msg;
    }

    public JsonResp() {
        super();
    }

    public JsonResp(JsonCodeEnum code) {
        super();
        setCode(code);
    }

    public JsonResp(JsonCodeEnum code, T data, boolean success, String msg) {
        super();
        setCode(code);
        this.data = data;
        this.success = success;
        this.msg = msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            msg = getCode().getMessage();
        }
        this.msg = msg;
    }

    public JsonCodeEnum getCode() {
        return code;
    }

    public void setCode(JsonCodeEnum code) {
        this.code = code;
        setMsg(code.getMessage());
        if (JsonCodeEnum.isSuccess(code)) {
            setSuccess(true);
        } else {
            setSuccess(false);
        }
    }

    public JsonResp(JsonCodeEnum code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public JsonResp<T> code(JsonCodeEnum code, String msg, T data) {
        return new JsonResp<T>(code, data, msg);
    }

    public JsonResp<?> code(JsonCodeEnum code, String msg) {
        return code(code, msg, null);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 当前对象转JSON数据
     * 参数callback不为空的情况下返回JSONp数据
     *
     * @return JSON/JSONp
     */
    public String toJson() {
        String callback = ServletThreadLocal.getCallbackParam();
        String json = JSON.toJSONString(this);
        if (!StringUtils.isBlank(callback)) {
            json = callback + "(" + json + ")";
        }
        return json;
    }

    @Override
    public String toString() {
        return toJson();
    }

    /**
     * 返回失败
     *
     * @param message 消息
     * @return toJson
     */
    public static JsonResp toFail(String message) {
        JsonResp json = getInstanceNew();
        json.setCode(JsonCodeEnum.FAIL);
        json.setMsg(message);
        return json;
    }

    /**
     * 空数据
     *
     * @return toJson
     */
    public static JsonResp isEmpty() {
        JsonResp json = getInstanceNew();
        json.setCode(JsonCodeEnum.EMPTY);
        json.setMsg("空数据");
        return json;
    }

    /**
     * 登陆超时
     *
     * @return toJson
     */
    public static JsonResp overtime() {
        JsonResp json = getInstanceNew();
        json.setCode(JsonCodeEnum.OVERTIME);
        json.setMsg("未登陆/登陆超时");
        return json;
    }

    /**
     * 返回失败
     *
     * @return toJson
     */
    public static JsonResp toFail() {
        return toFail(null);
    }

    /**
     * 自定义返回code
     *
     * @param code    JsonCodeEnum
     * @param message 消息
     * @return toJson
     */
    private String toJson(JsonCodeEnum code, String message) {
        setCode(code);
        setMsg(message);
        return toJson();
    }

    /**
     * 自定义返回code
     *
     * @param code JsonCodeEnum
     *             消息
     * @return toJson
     */
    public String toJson(JsonCodeEnum code) {
        return toJson(code, null);
    }

    /**
     * 失败消息（消息自定义）
     *
     * @param message 消息
     * @return toJson
     */
    public String fail(String message) {
        return toJson(JsonCodeEnum.FAIL, message);
    }

    /**
     * 失败消息
     *
     * @return 失败
     */
    public String fail() {
        setCode(JsonCodeEnum.FAIL);
        return toJson();
    }

    /**
     * 服务器出错了
     */
    public JsonResp error() {
        fail("服务器出错了");
        return this;
    }

    /**
     * 参数错误
     *
     * @param message 消息
     * @return toJson
     */
    public JsonResp parm(String message) {
        setCode(JsonCodeEnum.PARAMETER_INVALID);
        setMsg(message);
        return this;
    }

    /**
     * 失败消息
     *
     * @return 失败
     */
    JsonResp parm() {
        return parm(null);
    }

    /**
     * 成功消息
     *
     * @param message 消息
     * @return toJson
     */
    public String success(String message) {
        return toJson(JsonCodeEnum.SUCCESS, message);
    }

    /**
     * 成功消息
     *
     * @return toJson
     */
    public String success() {
        return toJson(JsonCodeEnum.SUCCESS, null);
    }

    /**
     * 返回成功
     *
     * @param data    数据
     * @param message 消息
     * @return toJson
     */
    private static <N> String toSu(N data, String message) {
        JsonResp<N> json = getInstanceNew();
        json.setCode(JsonCodeEnum.SUCCESS);
        json.setMsg(message);
        json.setData(data);
        return json.toJson();
    }

    /**
     * 自定义
     *
     * @param jsonCodeEnum code
     * @param message      消息
     * @return JSON
     */
    private static JsonResp toEnum(JsonCodeEnum jsonCodeEnum, String message) {
        JsonResp json = new JsonResp();
        json.setCode(jsonCodeEnum);
        json.setMsg(message);
        return json;
    }

    /**
     * 自定义
     *
     * @param jsonCodeEnum code
     * @return JSON
     */
    public static JsonResp toEnum(JsonCodeEnum jsonCodeEnum) {
        return toEnum(jsonCodeEnum, null);
    }

    /**
     * 返回成功
     *
     * @return toJson
     */
    public static String toSu(String message) {
        return toSu(null, message);
    }

    /**
     * 返回成功
     *
     * @return toJson
     */
    public static String toSu() {
        return toSu(null, null);
    }

    /**
     * 获取新实例
     *
     * @return 实例
     */
    private static <N> JsonResp<N> getInstanceNew() {
        return new JsonResp<>();
    }

    /**
     * 判断是否加载成功
     *
     * @return 成功/失败
     */
    public boolean loadSuccess() {
        if (code == JsonCodeEnum.SUCCESS) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置成功值
     *
     * @param message 消息
     */
    public void setSucc(String message, T data) {
        setData(data);
        setCode(JsonCodeEnum.SUCCESS);
        setMsg(message);
    }

    /**
     * 设置成功值
     *
     * @param message 消息
     */
    public void setSucc(String message) {
        setSucc(message, null);
    }

    /**
     * 设置成功值
     *
     * @param message 消息
     */
    public JsonResp<T> succ(String message) {
        return succ(message, null);
    }

    /**
     * 设置成功值
     *
     * @param message 消息
     */
    public JsonResp<T> succ(String message, T data) {
        setCode(JsonCodeEnum.SUCCESS);
        setMsg(message);
        setData(data);
        return this;
    }

    /**
     * 设置成功值
     */
    public JsonResp<T> succ(T data) {
        return succ(null, data);
    }

    /**
     * 设置成功值
     */
    public JsonResp<T> succ() {
        return succ("");
    }

    /**
     * 设置成功值
     */
    public void setSucc() {
        setSucc(null);
    }

    /**
     * 设置参数不合法值
     *
     * @param message 消息
     */
    public void setParm(String message) {
        setCode(JsonCodeEnum.PARAMETER_INVALID);
        setMsg(message);
    }

    /**
     * 设置参数不合法值
     */
    public void setParm() {
        setSucc(null);
    }

    /**
     * 数据为空
     *
     * @param message 消息
     * @return 自己
     */
    public JsonResp<T> empty(String message) {
        setCode(JsonCodeEnum.EMPTY);
        setMsg(message);
        return this;
    }

    /**
     * 数据为空
     *
     * @return 自己
     */
    public JsonResp empty() {
        return empty(null);
    }

    /**
     * 返回成功
     *
     * @param message 消息
     * @param data    数据
     * @return 新对象
     */
    @NotNull
    public static <E> JsonResp<E> ok(String message, E data) {
        return new JsonResp<E>().succ(message, data);
    }

    /**
     * 返回成功
     *
     * @param data 数据
     * @return 新对象
     */
    public
    @NotNull
    static <E> JsonResp<E> ok(E data) {
        return ok(null, data);
    }

    /**
     * 返回成功
     *
     * @param message 消息
     * @return 新对象
     */
    public static JsonResp<?> ok(String message) {
        return ok(message, null);
    }

    /**
     * 返回成功
     *
     * @param message 消息
     * @return 新对象
     */
    public static JsonResp okString(String message) {
        return ok(message, null);
    }


    /**
     * 返回成功
     *
     * @return 新对象
     */
    public static JsonResp ok() {
        return ok(null);
    }

    /**
     * 返回失败
     *
     * @param message 消息
     * @return 新对象
     */
    public static JsonResp fa(String message) {
        JsonResp json = new JsonResp();
        json.setCode(JsonCodeEnum.FAIL);
        json.setMsg(message);
        return json;
    }

    /**
     * 判断是否成功
     *
     * @return 自己
     */
    public JsonResp passSuccess() {
        if (loadSuccess()) {
            this.setSuccess(true);
        } else {
            this.setSuccess(false);
        }
        return this;
    }

    /**
     * 返回 数据空
     *
     * @param message 消息
     * @return 新对象
     */
    private static JsonResp toEmpty(String message) {
        JsonResp json = new JsonResp();
        json.setCode(JsonCodeEnum.EMPTY);
        json.setMsg(message);
        return json;
    }

    /**
     * 返回 数据空
     *
     * @return 新对象
     */
    static JsonResp toEmpty() {
        return toEmpty(null);
    }

    /**
     * list 是否空数据检测
     *
     * @param list    列表
     * @param message 消息
     * @return JSON
     */
    public static <N, L extends Collection<N>> JsonResp<L> list(L list, String message) {
        return new JsonResp<L>().collection(list, message);
    }

    /**
     * list 是否空数据检测
     *
     * @param list 列表
     * @return JSON
     */
    public static <N, L extends Collection<N>> JsonResp<L> list(L list) {
        return list(list, null);
    }

    public <N, L extends Collection<N>> JsonResp<L> collection(L list) {
        return collection(list, null);
    }

    public <N, L extends Collection<N>> JsonResp<L> collection(L list, String message) {
        JsonResp<L> collectionJson = new JsonResp<>();
        if (CollectionUtils.isEmpty(list)) {
            collectionJson.empty(message);
        } else {
            collectionJson.succ(list);
        }
        return collectionJson;
    }

    /**
     * 返回数据JSON
     *
     * @param ns       数据
     * @param emptyMsg 数组为空消息
     * @param <N>      数据类型
     */
    public static <N> JsonResp<N[]> array(N[] ns, String emptyMsg) {
        JsonResp<N[]> collectionJson = new JsonResp<>();
        if (ArrayUtils.isEmpty(ns)) {
            collectionJson.empty(emptyMsg);
        }
        return collectionJson.succ(ns);
    }

    /**
     * {@Link}array(N[]) 默认消息
     */
    public static <N> JsonResp<N[]> array(N[] ns) {
        return array(ns, null);
    }

    public JsonResp failRes(String message) {
        return setJsonCode(JsonCodeEnum.FAIL, message);
    }

    JsonResp failRes() {
        return failRes(null);
    }

    private JsonResp setJsonCode(JsonCodeEnum jsonCodeEnum, String message) {
        setCode(jsonCodeEnum);
        setMsg(message);
        return this;
    }

    /**
     * @param data    数据
     * @param message 数据为空消息
     * @return JSON
     */
    public JsonResp<T> data(T data, String message) {
        return data(data, message, null);
    }

    /**
     * @param data      数据
     * @param message   数据为空消息
     * @param okMessage OK消息
     * @return JSON
     */
    public JsonResp<T> data(T data, String message, String okMessage) {
        JsonResp<T> json = new JsonResp<>();
        if (data == null) {
            json.empty(message);
        } else {
            json.succ(okMessage, data);
        }
        return json;
    }


    public JsonResp<T> data() {
        return data(getData());
    }

    /**
     * @param data 数据
     * @return JSON
     */
    public JsonResp<T> data(T data) {
        return data(data, null);
    }

    /**
     * 登陆已失效或未登陆
     *
     * @param message 消息
     * @return this
     */
    public JsonResp login(String message) {
        return new JsonResp(JsonCodeEnum.OVERTIME, message);
    }

    /**
     * 登陆已失效或未登陆
     *
     * @return this
     */
    public JsonResp login() {
        return login("登陆已失效或未登陆");
    }

    /**
     * 分页，返回数据
     *
     * @return this
     */
    public static JsonResp dataPage(Page page) {
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), page.getRecords(), page.getTotal()));
    }

}