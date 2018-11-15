package com.rongke.utils;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2018/8/17.
 */
public class CreateCommOrderId {

    private static long orderNum = 0l;
    private static String date;

    public static String getOrderId() {
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

    public static void main(String args[]) throws Exception {
        String orderIdByTime = getOrderId();
        System.out.print(orderIdByTime);
    }
}
