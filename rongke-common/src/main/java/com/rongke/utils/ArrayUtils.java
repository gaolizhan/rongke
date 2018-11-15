package com.rongke.utils;

import java.util.Arrays;

/**
 * 一些常用的数组有关的帮助方法
 * Created by bin on 2017/2/20.
 */
public class ArrayUtils {

    public static Number max(Number... ns) {
        Arrays.sort(ns);
        return ns[ns.length - 1];
    }

    public static Number min(Number... ns) {
        Arrays.sort(ns);
        return ns[0];
    }

    /**
     * 是否数组是否为空,数据里面对象为空也返回true
     *
     * @param array 数组
     * @return 是=true
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || CollectionUtils.isEmpty(Arrays.asList(array));
    }

    /**
     * 数组合并
     *
     * @param first 第一个
     * @param rest  其他数组
     * @param <T>   类型
     * @return 合并后
     */
    @SafeVarargs
    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}
