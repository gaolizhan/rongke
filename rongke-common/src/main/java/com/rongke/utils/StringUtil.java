package com.rongke.utils;

/**
 * Author: D.Yang
 * Email: koyangslash@gmail.com
 * Date: 16/8/31
 * Time: 下午5:42
 * Describe: String工具类
 */
public class StringUtil {

    // 定义下划线
    private static final char UNDERLINE = '_';

    /**
     * String为空判断
     *
     * @param str 需判断字符串
     * @return true:为空 false:不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * String不为空判断
     *
     * @param str 需判断字符串
     * @return true:不为空 false:为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 驼峰转下划线工具
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String camelToUnderline(String param) {
        if (isNotEmpty(param)) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for (int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append(UNDERLINE);
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * 下划线转驼峰工具
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String underlineToCamel(String param) {
        if (isNotEmpty(param)) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for (int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (c == 95) {
                    ++i;
                    if (i < len) {
                        sb.append(Character.toUpperCase(param.charAt(i)));
                    }
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }


    /**
     * 在字符串两周添加''
     *
     * @param param 字符串
     * @return 处理后的字符串
     */
    public static String addSingleQuotes(String param) {
        return "\'" + param + "\'";
    }

    /**
     * 把字符串的后n位用“*”号代替
     *
     * @param str 要代替的字符串
     * @param n   代替的位数
     * @return
     */

    public static String replaceSubString(String str, int n) {
        String sub = "";
        try {
            sub = str.substring(0, str.length() - n);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < n; i++) {
                sb = sb.append("*");
            }
            sub += sb.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sub;
    }
}
