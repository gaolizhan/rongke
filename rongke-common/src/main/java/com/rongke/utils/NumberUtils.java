package com.rongke.utils;

import java.util.List;
import java.util.Random;


/**
 * @author <a href="mailto:rplees.i.ly@gmail.com">rplees</a>
 *         date 2010-04-07
 *         {@code} 数字类型的常用方法
 */
public class NumberUtils {
    private static final int DEFAULT_VALUE = 0;

    /**
     * 判断该数字是否为有效数字
     * 主要判断 是否为空和是否大于DEFAULT_VALUE
     *
     * @param n 数
     * @return true 为有效数字
     */
    public static boolean isValid(Number n) {
        if (n != null) {
            if (n instanceof Double) {
                return n.doubleValue() > 0D;
            } else if (n instanceof Float) {
                return n.floatValue() > 0L;
            } else if (n instanceof Long) {
                return n.longValue() > 0L;
            }

            return n.intValue() > DEFAULT_VALUE;
        } else {
            return false;
        }
    }

    /**
     * 判断该数字是否为无效数字
     *
     * @param n 数字
     * @return true为无效数字
     */
    public static boolean isNotValid(Number n) {
        return !isValid(n);
    }


    /**
     * 判断该数字是否为无效数字
     *
     * @param n 数字
     * @return true为无效数字
     */
    public static boolean isGt0Valid(Number n) {
        return (n != null && n.intValue() >= DEFAULT_VALUE);
    }

    /**
     * 去高位数保留指定长度的整数
     * cutTopNumber(7123456, 3) -->456
     *
     * @param n   目标数
     * @param len 保留的长度
     */
    public static Integer cutTopNumber(Integer n, int len) {
        if (n == null) return -1;
        String ns = String.valueOf(n);

        if (ns.length() <= len)
            return n;

        return Integer.parseInt(ns.substring(ns.length() - len, ns.length()));
    }

    /**
     * 对集合里面的数做 逻辑（|）的操作并返回
     *
     * @param ns Number...集合
     * @return Number
     */
    public static Number logic(Number... ns) {
        Number n = 0;
        for (Number number : ns) {
            n = n.intValue() | number.intValue();
        }
        return n;
    }

    /**
     * 对集合里面的数做 逻辑（|）的操作并返回
     *
     * @param nl List<Number>集合
     * @return Number
     */
    public static Number logic(List<? extends Number> nl) {
        Number n = 0;
        for (Number number : nl) {
            n = n.intValue() | number.intValue();
        }
        return n;
    }

    public static Number random() {
        return random(6);
    }

    public static void main(String[] args) {
        System.out.println(random(1));
    }

    public static Number random(int c) {
        Random r = new Random();
        double nextDouble = r.nextDouble();
        while (nextDouble < 0.1) {
            nextDouble = r.nextDouble();
        }

        double pow = Math.pow(10, c);
        return (int) (nextDouble * pow); //获取随机数
    }

    public static double setDecimal(double d, int len) {
        double rate = Math.pow(10, len);
        return ((long) (d * rate)) / rate;
    }

    public static long parseLong(String str, long defaultValue) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long parseLong(String str) {
        return parseLong(str, -1);
    }

    public static int parseInt(String str, int defaultInt) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defaultInt;
        }
    }

    public static int parseInt(String str) {
        return parseInt(str, -1);
    }

    private static double parseDouble(String str, double defaultDouble) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return defaultDouble;
        }
    }

    /**
     * 转换中文
     */
    public static String parseCH(int number) {
        String[] str = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        StringBuffer sb = new StringBuffer();
        String s = String.valueOf(number);
        for (int i = 0; i < s.length(); i++) {
            String index = String.valueOf(s.charAt(i));
            sb = sb.append(str[Integer.parseInt(index)]);
        }
        return sb.toString();
    }

    /**
     * 转换金钱
     */
    public static String parseCHMoney(int d) {
        String[] str = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String ss[] = new String[]{"元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿"};
        String s = String.valueOf(d);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            String index = String.valueOf(s.charAt(i));
            sb = sb.append(str[Integer.parseInt(index)]);
        }
        String sss = String.valueOf(sb);
        int i = 0;
        for (int j = sss.length(); j > 0; j--) {
            sb = sb.insert(j, ss[i++]);
        }
        return sb.toString();
    }

    public static double parseDouble(String str) {
        return parseDouble(str, 0d);
    }
}