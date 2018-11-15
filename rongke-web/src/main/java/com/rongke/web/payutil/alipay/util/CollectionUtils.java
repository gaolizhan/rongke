package com.rongke.web.payutil.alipay.util;
import java.util.*;

/**
 * @author <a href="mailto:rplees.i.ly@gmail.com">rplees</a>
 *         date 2010-04-06
 *         {@code} 一些常用的集合有关的帮助方法
 */
public class CollectionUtils {

    /**
     * 判断集合是否为空<br/>
     * 集合对象为空也判断<br/>
     *
     * @param collection 集合
     * @return true为空，false不为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        boolean empty = false;
        if (collection == null || collection.isEmpty()) empty = true;
        if (empty) {
            return true;
        } else {
            int i = 0;
            for (Object c : collection) {
                if (c == null) i++;
            }
            if (i == collection.size()) {
                empty = true;
            }
            return empty;
        }
    }

    /**
     * 判断Map集合是否为空
     *
     * @param m map
     * @return true为空，false不为空
     */
    public static boolean isEmpty(Map<?, ?> m) {
        return m == null || m.size() < 1;
    }

    public static boolean isEmpty(Object[] o) {
        return o == null || o.length < 1;
    }

    public static boolean isNotEmpty(Object[] o) {
        return !isEmpty(o);
    }

    /**
     * 判断集合是否不为空
     *
     * @return true为非空，false为空
     */
    public static boolean isNotEmpty(Map<?, ?> m) {
        return m != null && m.size() > 0;
    }

    /**
     * 判断Map集合是否不为空
     *
     * @return true为非空，false为空
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return coll != null && !coll.isEmpty();
    }

    /**
     * 将集合中的元素压平 1,2,3
     *
     * @param coll 集合
     * @return toString
     */
    public static String paserIdsListToString(Collection<?> coll) {
        StringBuffer sb = new StringBuffer();
        if (!coll.isEmpty()) {
            for (Iterator<?> iter = coll.iterator(); iter.hasNext(); ) {
                Long id = (Long) iter.next();
                if (null != id) {
                    sb.append(id);
                }
                if (iter.hasNext()) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 俩个list比较找出相对与后者来说不同的部分
     *
     * @param aList one
     * @param bList two
     * @return 不同的集合
     */
    public static <T> List<T> compareLatterFindDiff(List<T> aList, List<T> bList) {
        List<T> ret = new ArrayList<T>();
        if (aList == bList) {
            return null;
        }

        if (bList == null) {
            return null;
        }
        if (aList == null) {
            return bList;
        }

        for (T bObj : bList) {
            boolean isFind = false;
            for (T aObj : aList) {

                if (bObj == aObj || bObj.equals(aObj)) {
                    isFind = true;
                    break;
                }
            }

            if (!isFind) { //删除
                ret.add(bObj);
            }
        }

        return ret;
    }


    /**
     * 通过HashSet踢除重复元素除去List集合中的重复数据
     */
    public static <T> List<T> duplicate(List<T> list) {
        HashSet<T> h = new HashSet<>(list);
        list.clear();
        list.addAll(h);
        return list;
    }
}
