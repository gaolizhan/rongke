package com.rongke.commons;

/**
 * 用来表示是否是接送错误
 * Created by bin on 2017/2/20.
 */
public class JsonException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JsonResp getJsonResp() {
        return jsonResp;
    }

    public JsonException(JsonResp jsonResp) {
        super();
        this.jsonResp = jsonResp;
    }

    protected void setJsonResp(JsonResp jsonResp) {
        this.jsonResp = jsonResp;
    }

    public JsonException() {
        super();
    }

    public JsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(Throwable cause) {
        super(cause);
    }

    private JsonResp jsonResp = new JsonResp();

    private JsonException fial(String message) {
        setJsonResp(new JsonResp().failRes(message));
        return this;
    }

    public JsonException fial() {
        return fial(null);
    }

}
