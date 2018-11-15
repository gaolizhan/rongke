package com.rongke.utils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


public class StringTemplateUtils {

    public static final String DEF_REGEX="\\{(.+?)\\}";

    public static String render(String template, Map<String, String> data) {
        return render(template,data,DEF_REGEX);
    }

    public static String render(String template, Map<String, String> data,String regex) {
        if(StringUtils.isBlank(template)){
            return "";
        }
        if(StringUtils.isBlank(regex)){
            return template;
        }
        if(data == null || data.size() == 0){
            return template;
        }
        try {
            StringBuffer sb = new StringBuffer();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(template);
            while (matcher.find()) {
                String name = matcher.group(1);// 键名
                String value = data.get(name);// 键值
                if (value == null) {value = "";}
                matcher.appendReplacement(sb, value);
            }
            matcher.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return template;

    }

    public static void main(String args[]) throws ParseException {
        String template="您提现{borrowAmount}元至尾号{tailNo}的请求失败，您可以重新提交提款申请。";
        Map<String, String> data = new HashMap<String, String>();
        data.put("borrowAmount", "1000.00");
        data.put("tailNo", "1234");
        System.out.println(render(template,data));

        String message = MessageFormat.format("您好{0,8}，晚上好！您目前余额：{1,number,#}元，积分：{2}", "小王", 10.155, 10);
        System.out.println(message);
    }
}
