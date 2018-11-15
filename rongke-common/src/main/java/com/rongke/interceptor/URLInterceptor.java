package com.rongke.interceptor;

import com.rongke.annotation.LoginCheck;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chengwenwen on 2016/10/19.
 * URL拦截
 */
public class URLInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //LoginCheck Annotation，实现登陆验证
        LoginCheck loginCheck = ((HandlerMethod) handler).getMethodAnnotation(LoginCheck.class);
        if (loginCheck != null) {
            System.out.print("false");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
