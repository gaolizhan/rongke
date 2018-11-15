package com.rongke.commons;

/**
 * 参数错误异常
 */
public class ParameterException extends JsonException {

    public ParameterException() {
        super();
        setJsonResp(new JsonResp().parm());
    }

    public ParameterException(String message, Throwable cause) {
        super(message, cause);
        setJsonResp(new JsonResp().parm(message));
    }

    public ParameterException(String s) {
        super(s);
        setJsonResp(new JsonResp().parm(s));
    }

    public ParameterException(Throwable cause) {
        super(cause);
    }

}
