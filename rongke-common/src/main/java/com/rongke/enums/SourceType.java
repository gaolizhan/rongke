package com.rongke.enums;

import com.rongke.commons.FailException;
import com.rongke.commons.JsonResp;
import com.rongke.commons.ParameterException;
import com.rongke.sign.Sign;
import com.rongke.utils.Md5;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 请求来源 Created by jump on 2016/7/4.
 */
public enum SourceType {
    APP("APP"), ADMIN("后台管理"), WECHAT("微信"), H5("手机H5"), PC_WEB("电脑端浏览器");
    /**
     * 来源
     */
    public static final String PARAMETER_NAME_SOURCE = "source";
    /**
     * 来源
     */
    public static final String PARAMETER_NAME_TOKEN = "token";
    /**
     * 需要添加cookie的来源
     */
    public static final SourceType[] addCookie = new SourceType[]{ADMIN, WECHAT, H5, PC_WEB};
    /**
     * token的时长
     */
    public static final int tokenMaxAge = 3600 * 60 * 24 * 7;
    /**
     * 公钥， 私钥 对集合
     */
    public static final Map<String, String> KEY_PAIR = new HashMap<>();
    /**
     * 时间错相差分钟数
     */
    private static final Integer TIMESTAMP_DIFFER = 5;

    /**
     * 字段解释
     */
    private String explain;

    SourceType(String explain) {
        this.explain = explain;
    }

    public String getExplain() {

        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    /**
     * 添加COOKIE到登陆器
     *
     * @param token 令牌
     * @return 来源可添加cookie返回true
     */
    public static boolean addLoginCookie(HttpServletRequest request, HttpServletResponse response, String token) {
        SourceType parse = parse(request);
        return addLoginCookie(parse, response, token);
    }

    /**
     * 添加COOKIE到登陆器
     *
     * @param token 令牌
     * @return 来源可添加cookie返回true
     */
    public static boolean addLoginCookie(SourceType type, HttpServletResponse response, String token) {
        return addLoginCookie(type, response, token, null);
    }

    /**
     * 添加COOKIE到登陆器
     *
     * @param token 令牌
     * @return 来源可添加cookie返回true
     */
    public static boolean addLoginCookie(SourceType type, HttpServletResponse response, String token, Integer tokenMaxAge) {
        if (tokenMaxAge == null) tokenMaxAge = SourceType.tokenMaxAge;
        if (hasAddCookie(type)) {
            Cookie tokenCookie = new Cookie(genderCookieTokenName(type), token);
            tokenCookie.setMaxAge(tokenMaxAge);
            tokenCookie.setPath("/");
            response.addCookie(tokenCookie);
            return true;
        }
        return false;
    }

    /**
     * 获取token
     *
     * @return token
     */
    public static String getTokenForCookie(HttpServletRequest request) {
        SourceType sourceType = parse(request);
        return getTokenForCookie(request.getCookies(), sourceType);
    }

    /**
     * 获取token
     *
     * @param sourceType 来源
     * @return token
     */
    public static String getTokenForCookie(Cookie[] cookies, SourceType sourceType) {
        String tokenName = genderCookieTokenName(sourceType);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 是否需要添加COOKIE
     *
     * @param sourceType 来源
     * @return 需要rue
     */
    public static boolean hasAddCookie(SourceType sourceType) {
        for (SourceType source : addCookie) {
            if (sourceType.equals(source)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生产cookie名字
     *
     * @param source 来源
     * @return cookie名字
     */
    public static String genderCookieTokenName(SourceType source) {
        return source.name() + "_TOKEN";
    }

    /**
     * 字符串转枚举
     *
     * @param type 枚举NAME相同字符串
     * @return 字符串为空或不匹配返回NULL
     */
    public static SourceType parse(String type) {
        if (StringUtils.isBlank(type)) {
            return SourceType.H5;
        }
        SourceType[] values = SourceType.values();
        for (SourceType sourceType : values) {
            if (sourceType.name().equals(type)) {
                return sourceType;
            }
        }
        return null;
    }

    /**
     * 获取来源
     *
     * @return 来源
     */
    public static SourceType parse(HttpServletRequest request) {
        String source = request.getParameter(PARAMETER_NAME_SOURCE);
        SourceType sourceType = SourceType.parse(source);
        if (sourceType == null) {
            throw new ParameterException("来源参数[" + PARAMETER_NAME_SOURCE + "]不合法");
        }
        return sourceType;
    }

    /**
     * 验证其他参数是否合法
     *
     * @param request 参数+来源
     * @return 合法返回true
     */
    public static JsonResp validate(HttpServletRequest request) {
        return validate(request, null);
    }

    /**
     * 验证其他参数是否合法
     *
     * @param request    参数+来源
     * @param sourceType 如果为空从请求对象中获取
     * @return 合法返回true
     */
    public static JsonResp validate(HttpServletRequest request, SourceType sourceType) {
        //拿到source
        sourceType = sourceInit(request, sourceType);
        switch (sourceType) {
            case APP:
                String timestamp = request.getParameter(Sign.PARAMETER_NAME_TIMESTAMP);
                String appKey = request.getParameter(Sign.PARAMETER_NAME_APP_KEY);
                String sign = request.getParameter(Sign.PARAMETER_NAME_SIGN);
                JsonResp jsonResp = new JsonResp();
                if (StringUtils.isBlank(timestamp)) {
                    return jsonResp.parm("时间戳不能为空");
                }
                if (StringUtils.isBlank(appKey)) {
                    return jsonResp.parm("私钥不能为空");
                }
                if (StringUtils.isBlank(sign)) {
                    return jsonResp.parm("签名不能为空");
                }
                Long timestampLong;
                try {
                    timestampLong = Long.parseLong(timestamp);
                } catch (Exception e) {
                    throw new ParameterException("时间戳不合法");
                }
                if ((System.currentTimeMillis() - timestampLong) / (1000 * 60) > TIMESTAMP_DIFFER) {
                    return jsonResp.parm("时间戳相差超过5分钟请重新请求");
                }
                String secretKey = KEY_PAIR.get(appKey);
                if (Objects.equals(secretKey, null)) {
                    if (KEY_PAIR.isEmpty()) throw new FailException("未配置私钥对，请联系管理员");
                    return jsonResp.parm("私钥不正确");
                }
                //String allParameter = Sign.makeSignAllParameter(request);
                String allParameter = "APP";
                allParameter = appKey + secretKey + timestamp + allParameter;
                String md5 = Md5.md5Encode(allParameter);
                if (!Md5.md5Encode(allParameter).equals(sign)) {
                    return jsonResp.parm("签名不正确");
                }
                break;
            case ADMIN:
                return null;
        }
        return null;
    }


    /**
     * 获取来源 如果为空从请求对象中获取
     *
     * @param request    请求对象
     * @param sourceType 来源
     * @return 最终来源
     */
    public static SourceType sourceInit(HttpServletRequest request, SourceType sourceType) {
        if (sourceType == null) {
            sourceType = parse(request);
        }
        return sourceType;
    }

    /**
     * 获取登陆令牌 参数
     *
     * @param request 请求对象
     * @return token
     */
    public static String getTokenByParameter(HttpServletRequest request) {
        return request.getParameter(PARAMETER_NAME_TOKEN);
    }

    /**
     * 根据来源获取TOKEN
     *
     * @param request    请求对象
     * @param sourceType 来源
     * @return 令牌
     */
    public static String getToken(HttpServletRequest request, SourceType sourceType) {
        sourceType = sourceInit(request, sourceType);
        if (hasAddCookie(sourceType)) {
            return getTokenForCookie(request.getCookies(), sourceType);
        } else {
            return getTokenByParameter(request);
        }
    }

    /**
     * 根据来源获取TOKEN
     *
     * @param request 请求对象
     * @return 令牌
     */
    public static String getToken(HttpServletRequest request) {
        return getToken(request, null);
    }

}
