package com.rongke.utils;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 请求对象 工具类
 */
public class RequestUtils {

    /**
     * EG: /TEST/1.HTML 1.HTML
     *
     * @param request 请求对象
     * @return ServletPath
     */
    public static String getUri(ServletRequest request) {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String requestURI = servletRequest.getRequestURI();
        String contextPath = servletRequest.getContextPath();
        if (StringUtils.isBlank(contextPath)) {
            return requestURI;
        }
        return requestURI.substring(contextPath.length());
    }

    /**
     * 获取域名/ip地址
     * eg:  http://abc.cn:8080:/admin
     * http://abc.cn/admin
     * http://abc.cn
     *
     * @param request 请求对象
     * @return 主键地址
     */
    public static String getHostPath(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().
                getContextPath()).toString();
    }

    /**
     * 获取项目就到绝对路径
     * EG
     * 结果：D:\resin\webApps\TEST
     *
     * @param servletContext 请求对象
     * @return 绝对路径
     */
    public static File getProjectAbsolutePath(ServletContext servletContext) {
        return new File(servletContext.getRealPath(".")).getParentFile();
    }

    /**
     * 检测上传文件目录是否在项目路径下
     *
     * @param request 请求对象
     * @param file    文件对象
     * @return 是=true
     */
    public static boolean isInProjectAbsolutePath(HttpServletRequest request, File file) {
        return FileUtils.isInSubDirectory(getProjectAbsolutePath(request.getServletContext()), file);
    }


}
