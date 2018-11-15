package com.rongke.web.payutil.wxpay.util;

import com.rongke.commons.JsonResp;
import com.rongke.web.payutil.alipay.config.AlipayConfig;
import com.rongke.web.payutil.alipay.util.SignUtils;
import com.rongke.web.payutil.wxpay.api.WXpayApi;
import com.rongke.web.payutil.wxpay.config.WXpayConfig;
import org.jdom.JDOMException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * 支付基本信息
 * @author zhangli
 * @date 2016年3月28日 下午2:35:24
 *
 *
 * 1.需要支付的信息  ：
 *         用户支付  支付相关业务 支付金额  回调业务
 * 2.第三方支付需要的信息：
 *         trade_no,subject,body,total_fee,notify_url
 *
 */
public class PayUtils {

    public static String sign(String content, String RSA_PRIVATE) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }
    public static String wxnotify = "/api/pay/wxpay/succ";

    /**
     *  支付宝支付
     * @param trade_no      订单号
     * @param total_fee     金额
     * @param subject       标题
     * @param body          内容
     * @param notify_url    回调地址
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String AliPay(String trade_no, String passback_params, Double total_fee, String subject, String body, String notify_url) throws UnsupportedEncodingException{
        //JSONObject json = new JSONObject();
       /* Map<String,Object>map=new HashMap<>();
        notify_url = URLEncoder.encode(notify_url, "utf-8");
        passback_params = URLEncoder.encode(passback_params, "utf-8");
        String orderInfo = "_input_charset=\"utf-8\"";
        orderInfo += "&body=" + "\"" + body + "\"";
        orderInfo += "&it_b_pay=\"30m\"";
        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";
        orderInfo += "&out_trade_no=" + "\"" + trade_no + "\"";
        orderInfo += "&partner=" + "\"" + AlipayConfig.partner + "\"";
        orderInfo += "&payment_type=\"1\"";
        orderInfo += "&return_url=\"" + notify_url + "\"";
        orderInfo += "&seller_id=" + "\"" + AlipayConfig.seller_email + "\"";
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        orderInfo += "&subject=" + "\"" + subject + "\"";
       // orderInfo += "&passback_params=" + "\"" + passback_params + "\"";
        String format = String.format("%.2f", total_fee);
        orderInfo += "&total_fee=" + "\"" + format+"\"";
        System.out.println(orderInfo);
        String sign = sign(orderInfo, AlipayConfig.ali_private_key);
        sign = URLEncoder.encode(sign, "utf-8");
        map.put("orderInfo",orderInfo);
        map.put("sign",sign);
        map.put("sign_type","RSA2");
        //return orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";
        return JsonResp.ok(map);*/

        String app_id=" 2018030102295371";
        String method="api/user/alipay";
        passback_params = URLEncoder.encode(passback_params, "utf-8");
        notify_url = URLEncoder.encode(notify_url, "utf-8");

        String orderInfo = "app_id=\""+app_id+"\"";
        orderInfo += "&method=\""+method+"\"";
        orderInfo += "&format=\"JSON\"";
        orderInfo += "&product_code=\"QUICK_MSECURITY_PAY\"";
        orderInfo += "&charset=\"utf-8\"";
        orderInfo += "&body=" + "\"" + body + "\"";
        orderInfo += "&it_b_pay=\"30m\"";
        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";
        orderInfo += "&version=\"1.0\"";
        orderInfo += "&out_trade_no=" + "\"" + trade_no + "\"";
        orderInfo += "&partner=" + "\"" + AlipayConfig.partner + "\"";
        orderInfo += "&payment_type=\"1\"";
        orderInfo += "&seller_id=" + "\"" + AlipayConfig.seller_email + "\"";
       // orderInfo += "&service=\"mobile.securitypay.pay\"";
        orderInfo += "&subject=" + "\"" + subject + "\"";
        orderInfo += "&passback_params=" + "\"" + passback_params + "\"";
        String format = String.format("%.2f", total_fee);
        orderInfo += "&total_fee=" + "\"" + format+"\"";
        System.out.println(orderInfo);
        String sign = sign(orderInfo, AlipayConfig.merchant_private_key);
        sign = URLEncoder.encode(sign, "utf-8");
        return orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA2\"";




    }


    /**
     *  微信支付
     * @param trade_no      订单号
     * @param attach        自定义参数 length=127
     * @param total_fee     金额
     * @param body          文字内容说明
     * @param wxnotify      回调地址
     */
    public static JsonResp WxPay(String trade_no, String attach, Double total_fee, String body, String wxnotify){
        Map<String,Object>map=new HashMap<>();
        //JSONObject json = new JSONObject();
        System.out.println(" =============》预付款开始:");
        String format = String.format("%.2f", total_fee);
        BigDecimal total_fee_m = new BigDecimal(format).multiply(new BigDecimal("100"));
        Map<String, String> retMap = testUnifiedorder(trade_no, attach, body, String.valueOf(total_fee_m.intValue()), wxnotify);
        System.out.println(" =============》预付款结束:");
        System.out.println(WXpayCore.isRetSuccess(retMap)); // 判断统一下单（预支付）接口是否成功
        if (WXpayCore.isRetSuccess(retMap)) {
            Map<String, String> appMap = WXpayApi.makePaymentMap(retMap);
            // 预支付成功，组装真正支付需要的参数，返回给app使用
            System.out.println(" =============》组装app使用参数:");
            System.out.println(appMap);
            map.put("sign", appMap.get("sign"));
            map.put("timestamp", appMap.get("timestamp"));
            map.put("partnerid", appMap.get("partnerid"));
            map.put("noncestr", appMap.get("noncestr"));
            map.put("prepayid", appMap.get("prepayid"));
            map.put("appid", appMap.get("appid"));
            map.put("package", "Sign=WXPay");
        } else {
            System.out.println(WXpayCore.getErrMsg(retMap));
        }
        return JsonResp.ok(map);
    }



    public static Map<String, Object> toPay(BigDecimal totalAmount,String description,String openId,  String trade_no,HttpServletRequest request) {
        String sym = request.getRequestURL().toString().split("/api/")[0];
        Map<String, String> map = weixinPrePay(trade_no,totalAmount,description,openId,sym,request);
        SortedMap<String, Object> finalpackage = new TreeMap<String, Object>();
        finalpackage.put("appId", WXpayConfig.WXAPPID);
        finalpackage.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        finalpackage.put("nonceStr", WXpayConfig.getRandomString(32));
        finalpackage.put("package", "prepay_id="+map.get("prepay_id"));
        finalpackage.put("signType", "MD5");
        String sign = WXpayConfig.createSign("UTF-8", finalpackage);
        finalpackage.put("paySign", sign);
        return finalpackage;
    }

    /**
     * pc端
     * @param trade_no
     * @param attach
     * @param total_fee
     * @param body
     * @param wxnotify
     * @return
     */
    public static Map<String, String> WxPay3(String trade_no, String attach, Double total_fee, String body, String wxnotify){
        Map<String,String>map=new HashMap<>();
        //JSONObject json = new JSONObject();
        System.out.println(" =============》预付款开始:");
        String format = String.format("%.2f", total_fee);
        BigDecimal total_fee_m = new BigDecimal(format).multiply(new BigDecimal("100"));
        Map<String, String> retMap = testUnifiedorder3(trade_no, attach, body, String.valueOf(total_fee_m.intValue()), wxnotify);
        System.out.println(" =============》预付款结束:");
        System.out.println(WXpayCore.isRetSuccess(retMap)); // 判断统一下单（预支付）接口是否成功
        if (WXpayCore.isRetSuccess(retMap)) {
            Map<String, String> appMap = WXpayApi.makePaymentMap(retMap);
            // 预支付成功，组装真正支付需要的参数，返回给app使用
            System.out.println(" =============》组装app使用参数:");
            System.out.println(appMap);
            map.put("sign", appMap.get("sign"));
            map.put("timestamp", appMap.get("timestamp"));
            map.put("partnerid", appMap.get("partnerid"));
            map.put("noncestr", appMap.get("noncestr"));
            map.put("prepayid", appMap.get("prepayid"));
            map.put("appid", appMap.get("appid"));
            map.put("code_url", retMap.get("code_url"));
            map.put("package", "Sign=WXPay");
        } else {
            System.out.println(WXpayCore.getErrMsg(retMap));
        }
        return map;
    }
    /**
     *  微信支付组装app使用参数
     * @param trade_no      订单号
     * @param body          文字内容说明
     * @param total_fee     金额
     * @param wxnotify      回调地址
     * @return
     */
    private static Map<String, String> testUnifiedorder(String trade_no, String attach,String body, String total_fee, String wxnotify) {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("body", body);                      // 商品描述
        testMap.put("attach", attach);                  // 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据 String(127)
        testMap.put("out_trade_no", trade_no);          // 商户订单号
        testMap.put("total_fee", total_fee);            // 总金额
        testMap.put("spbill_create_ip", "192.168.0.1"); // 终端IP
        testMap.put("notify_url", wxnotify);            // 通知地址
        testMap.put("trade_type", "APP");               // 交易类型
        return WXpayApi.unifiedOrderRetMap(testMap);
    }



    public static Map<String, String> weixinPrePay(String trade_no,BigDecimal totalAmount,
                                                   String description, String openid,String sym, HttpServletRequest request) {
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", WXpayConfig.WXAPPID);
        parameterMap.put("mch_id", WXpayConfig.wxsh);
        parameterMap.put("nonce_str", WXpayConfig.getRandomString(32));
        parameterMap.put("body", description); //商品描述
        parameterMap.put("out_trade_no", trade_no);  //商户订单号
        parameterMap.put("fee_type", "CNY");
        BigDecimal total = totalAmount.multiply(new BigDecimal(100));
        java.text.DecimalFormat df=new java.text.DecimalFormat("0");
        parameterMap.put("total_fee", df.format(total));
        parameterMap.put("spbill_create_ip","192.168.0.1");//request.getRemoteAddr()
        parameterMap.put("notify_url", sym + wxnotify);
        parameterMap.put("trade_type", "JSAPI");
        //trade_type为JSAPI是 openid为必填项
        parameterMap.put("openid", openid);
      //  System.out.println("");
        String sign = WXpayConfig.createSign("UTF-8", parameterMap);
        System.out.println("jiner2");
        parameterMap.put("sign", sign);
        String requestXML = WXpayConfig.getRequestXml(parameterMap);
        System.out.println(requestXML);
        String result = WXpayConfig.httpsRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",requestXML);
        System.out.println(".............................................................................."+result);
        Map<String, String> map = null;
        try {
            map = WXpayConfig.doXMLParse(result);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }
    /**
     *  微信扫码支付
     * @param trade_no      订单号
     * @param body          文字内容说明
     * @param total_fee     金额
     * @param wxnotify      回调地址
     * @return
     */
    private static Map<String, String> testUnifiedorder3(String trade_no, String attach,String body, String total_fee, String wxnotify) {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("body", body);                      // 商品描述
        testMap.put("attach", attach);                  // 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据 String(127)
        testMap.put("out_trade_no", trade_no);          // 商户订单号
        testMap.put("total_fee", total_fee);            // 总金额
        testMap.put("spbill_create_ip", "192.168.0.1"); // 终端IP
        testMap.put("notify_url", wxnotify);            // 通知地址
        testMap.put("trade_type", "NATIVE");               // 交易类型
        return WXpayApi.unifiedOrderRetMap(testMap);
    }

}
