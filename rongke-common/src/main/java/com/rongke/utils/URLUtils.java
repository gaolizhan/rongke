package com.rongke.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * URL工具类
 */
public class URLUtils {

    /**
     * URL分隔符
     */
    public static final String URL_SPLIT = "/";

    /**
     * URL把2个或多个'/' 转成1个'/'<br>
     * EG: ///1.HTML --> /1.HTML
     *
     * @param url 地址
     * @return '/'
     */
    public static String slashParse(String url) {
        return url.replaceAll("//*", URL_SPLIT);
    }

    /**
     * 将驼峰命名规范转换成URI规范
     * eg:
     * 原：getUserInfo
     * 后:/get/user/info
     *
     * @param hump 驼峰字符串
     * @return uri
     */
    public static String humpParseUri(String hump) {
        List<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < hump.length(); i++) {
            char humpChar = hump.charAt(i);
            boolean isNumber = true;
            try {
                int i1 = Integer.parseInt(humpChar + "");
                if (!NumberUtils.isValid(i1)) {
                    throw new RuntimeException("不是一个数字");
                }
            } catch (Exception e) {
                isNumber = false;
            }
            if (!isNumber) {
                if (!Character.isLowerCase(humpChar)) {
                    indexes.add(i);
                }
            }
        }
        String uri = "";
        if (indexes.size() != 0) {
            for (int i = 0; i < indexes.size(); i++) {
                Integer index = indexes.get(i);
                Integer startIndex = 0;
                if (i != 0) {
                    startIndex = indexes.get(i - 1);
                }
                uri += URL_SPLIT + hump.substring(startIndex, index).toLowerCase();
            }

            uri += URL_SPLIT + hump.substring(indexes.get(indexes.size() - 1), hump.length()).toLowerCase();

            if (!Character.isLowerCase(hump.charAt(0))) {
                uri = uri.substring(1);
            }
        } else {
            return URL_SPLIT + hump;
        }
        return uri;
    }


    /**
     * URL拼接
     * EG：user,login
     * user/login
     *
     * @param uris URI
     * @return URL
     */
    public static String urlMosaic(String... uris) {
        StringBuilder sb = new StringBuilder();
        if (!ArrayUtils.isEmpty(uris)) {
            for (String uri : uris) {
                sb.append(uri).append(URL_SPLIT);
            }
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }

        return sb.toString();
    }

    /**
     * 将文件路径转换成URi
     * RG
     * win下 e\\upload\\s -> e/upload/s
     *
     * @return URI
     */
    public static String filePathParseUri(String finalDir) {
        String separator = File.separator;
        if (separator.equals("\\")) {
            separator = "\\\\";
        }
        String[] split = finalDir.split(separator);
        String uriAddress = "";
        for (int i = 1; i < split.length; i++) {
            String uri = split[i];
            uriAddress += uri + URL_SPLIT;
        }
        return URL_SPLIT + uriAddress;
    }

    /**
     * 将URI转换成文件路径
     * RG
     * win下 e/upload/s -> e\\upload\\s
     *
     * @return 文件路径
     */
    public static String uriParseFilePath(String uri) {
        return uri.replace(URL_SPLIT, File.separator);
    }

    /**
     * 对Map内所有value作utf8编码，拼接返回结果
     * 针对location参数的改进
     *
     * @param data 数据
     * @return 拼接
     * @throws UnsupportedEncodingException 编码失败
     */
    public static String toQueryString(Map<String, Object> data)
            throws UnsupportedEncodingException {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<?, ?> pair : data.entrySet()) {
            queryString.append(pair.getKey()).append("=");
            String ss[] = pair.getValue().toString().split(",");
            if (ss.length > 1) {
                for (String s : ss) {
                    queryString.append(URLEncoder.encode(s, "UTF-8")).append(",");
                }
                queryString.deleteCharAt(queryString.length() - 1);
                queryString.append("&");
            } else {
                queryString.append(URLEncoder.encode(pair.getValue().toString(),
                        "UTF-8")).append("&");
            }
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

}
