package com.rongke.commons;

/**
 * 失败异常
 * Created by bin on 2017/2/20.
 */
public class FailException extends JsonException {
    private static final long serialVersionUID = 1L;

    public FailException() {
        super();
        setJsonResp(new JsonResp().failRes());
    }

    public FailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        setJsonResp(new JsonResp().failRes(message));
    }

    public FailException(String message, Throwable cause) {
        super(message, cause);
        setJsonResp(new JsonResp().failRes(message));
    }

    public FailException(String message) {
        super(message);
        setJsonResp(new JsonResp().failRes(message));
    }

    public FailException(Throwable cause) {
        super(cause);
    }

}
