package com.rongke.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by 11829 on 2017/6/29.
 */
public class RequestUtil {
    /**
     * 获取Request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 获取session
     *
     * @return
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

}
