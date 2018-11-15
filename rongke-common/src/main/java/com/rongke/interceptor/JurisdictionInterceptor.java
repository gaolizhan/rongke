package com.rongke.interceptor;


import com.rongke.annotation.SourceAuthority;
import com.rongke.commons.JsonException;
import com.rongke.commons.JsonResp;
import com.rongke.enums.SourceType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.security.Principal;

import static com.rongke.enums.SourceType.PARAMETER_NAME_SOURCE;


/**
 * 接口权限拦截器
 *
 * @author 刘兴
 */
public class JurisdictionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //sourceAuthority Annotation，实现source验证
        if (handler instanceof HandlerMethod) {
            SourceAuthority sourceAuthority = ((HandlerMethod) handler).getMethodAnnotation(SourceAuthority.class);
            String sortType = request.getParameter(PARAMETER_NAME_SOURCE);
            if (sourceAuthority != null) {
                if (!sourceAuthority.value()[0].getExplain().equals(sortType)) {
                    return false;
                }
            }
        }
        JsonResp validate;
        try {
            validate = SourceType.validate(request);
        } catch (JsonException e) {
            PrintWriter writer = response.getWriter();
            writer.print(e.getJsonResp().toJson());
            writer.close();
            return false;
        }
        if (validate != null) {
            PrintWriter writer = response.getWriter();
            writer.print(validate.toJson());
            writer.close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}