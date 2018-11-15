package com.rongke.web.payutil.wxpay.config;


/* *
 *类名：WXpayConfig
 *功能：微信支付相关配置
 */

import com.rongke.wx.wxutils.MD5Util;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class WXpayConfig {
    // 发送内容的编码格式
    public static final String DEFAULT_CHAR_SET = "UTF-8";

    // 接收返回内容的编码格式
    public static final String DEFAULT_RESP_CHAR_SET = "UTF-8";

    // 统一下单接口中使用 trade_type
    public static final String DEFAULT_TRADE_TYPE = "APP";

    // 拼接真正支付使用参数用的，目前固定为 Sign=WXPay
    public static final String PACKAGE = "Sign=WXPay";

    // ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // APPid
    public static String appid = "wxf6b618e15ac70ba7";
    // API 密钥
    public static String api_key = "uhbygvtfcrdx951627843wsxedcrfvij";
    // 商户号
    public static String mch_id = "1493993892";

    //微信公众号配置信息
    public  static  String wxsh="1485608052";
    public  static  String WXAPPID="wx242f8ebb59f20be1";
    public  static  String  appSecret="1164cf4fec9e43bf988339b65bf4eb99";
    public  static  String wxapikey="qqqqq12345XXXXX54321MMMMM12345ss";

    // ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    public static final String unifiedorder_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String orderquery_url = "https://api.mch.weixin.qq.com/pay/orderquery";


    //微信端支付 随机生成的字符串长度
    //随机字符串生成
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    //微信端支付生成签名
    //生成签名
    public static String createSign(String characterEncoding,SortedMap<String,Object> parameters){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + wxapikey);
        System.out.println(sb.toString());
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }



    //XML
    //请求xml组装
    public static String getRequestXml(SortedMap<String,Object> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            if ("attach".equalsIgnoreCase(key)||"body".equalsIgnoreCase(key)||"sign".equalsIgnoreCase(key)) {
                sb.append("<"+key+">"+"<![CDATA["+value+"]]></"+key+">");
            }else {
                sb.append("<"+key+">"+value+"</"+key+">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    //请求方法
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}"+ ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}"+ e);
        }
        return null;
    }



    //xml解析
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if(null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }





    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

}
