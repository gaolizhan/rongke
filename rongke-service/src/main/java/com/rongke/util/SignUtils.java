package com.rongke.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SignUtils {

    private static final String objectClass = "java.lang.Object";

    /**
       私钥文件路径
	 */
    public static final String privateKey = "key/pri-fengkong.key";


    final public static List<String> toArray(Object target){

        List<String> res = new ArrayList<String>();
        Class thisClass = target.getClass();
        Field[] fields = new Field[0];
        while (!objectClass.equals(thisClass.getName())) {
            Field[] fieldTemp = thisClass.getDeclaredFields();
            thisClass = thisClass.getSuperclass();
            fields = ArrayUtils.addAll(fields, fieldTemp);
        }
        for (Field field : fields) {
            Object obj;
            try {
                field.setAccessible(true);
                obj = field.get(target);
                if(field.getGenericType() instanceof ParameterizedType){
                    ParameterizedType pt = (ParameterizedType) field.getGenericType();
                    if(pt.getRawType().equals(List.class)){
                        for(Object items : (List)obj){
                            res.addAll(toArray(items));
                        }
                    }
                } else if (obj != null && StringUtils.isNotEmpty(obj.toString())) {
                    if("sign".equals(field.getName())
                            ||"riskData".equals(field.getName())
                            ||"log".equals(field.getName())
                            ||"serialVersionUID".equals(field.getName())) {
                        continue;
                    }
                    res.add(field.getName() + obj.toString());
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    final public static String getSign(Object object){
        List<String> requestMap = SignUtils.toArray(object);
        //私钥加密
        String sign = SignatrueUtil.getSign(requestMap,privateKey);
        return sign;
    }

    /**
     * @param object
     * @return
     */
    final public static String getSlpSign(Object object){
        List<String> requestMap = SignUtils.toArray(object);
        //私钥加密
        String sign = SignatrueUtil.getSign(requestMap,privateKey);
        return sign;
    }

    public static void main(String[] args) {
        getSign(new Object());
    }
}
