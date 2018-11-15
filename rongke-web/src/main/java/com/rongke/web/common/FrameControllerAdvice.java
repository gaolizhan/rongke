package com.rongke.web.common;

import com.rongke.commons.JsonCodeEnum;
import com.rongke.commons.JsonResp;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * 错误代理
 * Created by Administrator on 2017/3/26.
 */
@ControllerAdvice
public class FrameControllerAdvice {
    private Logger logger = Logger.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object processUnauthenticatedException(NativeWebRequest request, Exception e) {
        if (null != e.getMessage() && e.getMessage().contains("The current request is not a multipart request")) {
            e.printStackTrace();
            return "";
        }
        if (null != e.getMessage()) {
            logger.error(e.getMessage());
        }
        JsonResp jsonResp;
        if (JsonCodeEnum.OVERTIME.getMessage().equals(e.getMessage())) {
            jsonResp = JsonResp.overtime();
        } else {
            jsonResp = JsonResp.toFail(e.getMessage());
            e.printStackTrace();
        }
        return jsonResp;
    }
}
