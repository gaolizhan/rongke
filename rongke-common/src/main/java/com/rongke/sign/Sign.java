package com.rongke.sign;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 接口签名
 * Created by jump on 2016/7/5.
 */
public class Sign {

    /**
     * 时间戳
     */
    public static final String PARAMETER_NAME_TIMESTAMP = "timestamp";
    /**
     * 公钥
     */
    public static final String PARAMETER_NAME_APP_KEY = "appKey";
    /**
     * 签名
     */
    public static final String PARAMETER_NAME_SIGN = "sign";
    /**
     * 加密所需的参数
     */
    private static final String[] PARAMETER_NAMES = new String[]{PARAMETER_NAME_SIGN, PARAMETER_NAME_TIMESTAMP,
            PARAMETER_NAME_APP_KEY};

    /**
     * <pre>
     * 		原 app_key=111&sign=222&token=123<br>
     * 		结果 token=123
     * </pre>
     *
     * @param request 请求对象
     * @return 其他参数
     * @since JDK 1.6
     */
    public static String removeSignAuthUrlParameter(HttpServletRequest request) {
        String sign = "";
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String value = parameterNames.nextElement();
            if (!value.equalsIgnoreCase(PARAMETER_NAME_SIGN) && !value.equalsIgnoreCase(PARAMETER_NAME_APP_KEY)
                    && !value.equalsIgnoreCase(PARAMETER_NAME_TIMESTAMP)) {
                sign += value + "=";
                sign += request.getParameter(value) + "&";
            }
        }

        // 去除最后一位 &
        try {
            String lastSign = sign.substring(sign.length() - 1);
            if (lastSign.equals("&")) {
                sign = sign.substring(0, sign.length() - 1);
            }
        } catch (Exception e) {
            return sign;
        }
        return sign;
    }

    /**
     * <pre>
     *  参数	token=token123&name=name321&app_key=111&sign=222<br>
     * 	返回格式 ：   token123name321
     * </pre>
     *
     * @return 其他参数
     */
    public static String makeSignAllParameter(HttpServletRequest request) {
        String sign = "";
        String method = request.getMethod();
        if (method.equalsIgnoreCase("post")) {
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
                String name = params.nextElement();
                boolean isAnd = true;
                for (String parameter : PARAMETER_NAMES) {
                    if (name.equalsIgnoreCase(parameter)) {
                        isAnd = false;
                        break;
                    }
                }
                if (isAnd) sign += request.getParameter(name);
            }
        } else if (method.equalsIgnoreCase("get")) {
            String pars = request.getQueryString();
            if (pars != null) {
                String[] split = pars.split("&");
                for (String cv : split) {
                    String[] kv = cv.split("=");
                    if (kv.length >= 2) {
                        String key = kv[0];
                        boolean isAnd = true;
                        for (String parameter : PARAMETER_NAMES) {
                            if (key.equalsIgnoreCase(parameter)) {
                                isAnd = false;
                                break;
                            }
                        }
                        if (isAnd) {
                            sign += kv[1];
                        }
                    }
                }
            }
        }
        return sign;
    }
}
