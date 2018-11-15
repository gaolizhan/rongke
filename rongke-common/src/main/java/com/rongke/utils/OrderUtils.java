package com.rongke.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chengwenwen on 2016/12/20.
 */
public class OrderUtils {
    private static long orderNum = 0l;
    private static long udInfoOrderNum = 0l;
    private static String date;


    /**
     * 生成订单编号
     *
     * @return
     */
    public static synchronized String getOrderNo() {
        String str = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        if (date == null || !date.equals(str)) {
            date = str;
            orderNum = 0l;
        }
        orderNum++;
        long orderNo = Long.parseLong((date)) * 100000;
        orderNo += orderNum;
        return orderNo + "";
    }

    /**
     * 生成订单编号
     *
     * @return
     */
    public static synchronized String getUdOrderNo() {
        String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        if (date == null || !date.equals(str)) {
            date = str;
            udInfoOrderNum = 0l;
        }
        udInfoOrderNum++;
        long orderNo = Long.parseLong((date)) * 100000;
        orderNo += udInfoOrderNum;
        return orderNo + "";
    }

    /**
     * 余额支付交易订单编号
     *
     * @return
     */
    public static synchronized String getBalancePayNo() {
        String str = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        if (date == null || !date.equals(str)) {
            date = str;
            orderNum = 0l;
        }
        orderNum++;
        long orderNo = Long.parseLong((date)) * 10000;
        orderNo += orderNum;
        return "xx4009772001" + orderNo + "";
    }

    /**
     * 生成易宝订单编号
     *
     * @return
     */
    public static synchronized String getYdOrderNo() {
        String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        if (date == null || !date.equals(str)) {
            date = str;
            orderNum = 0l;
        }
        orderNum++;
        long orderNo = Long.parseLong((date)) * 100000;
        orderNo += orderNum;
        return orderNo + "";
    }
}
