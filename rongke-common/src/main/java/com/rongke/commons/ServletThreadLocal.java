package com.rongke.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 把response和request存入当前线程容器中
 * Created by bin on 2017/2/20.
 */
public class ServletThreadLocal {
    private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<>();

    public static HttpServletRequest getRequest() {
        return requestLocal.get();
    }

    public static void setRequest(HttpServletRequest request) {
        requestLocal.set(request);
    }

    public static HttpServletResponse getResponse() {
        return responseLocal.get();
    }

    public static void setResponse(HttpServletResponse response) {
        responseLocal.set(response);
    }

    public static HttpSession getSession() {
        return requestLocal.get().getSession();
    }

    /**
     * 获取JSONBp参数
     *
     * @return 参数名
     */
    public static String getCallbackParam() {
        String callback = null;
        if (getRequest() != null) {
            callback = getRequest().getParameter("callback");
        }
        return callback;
    }
}
