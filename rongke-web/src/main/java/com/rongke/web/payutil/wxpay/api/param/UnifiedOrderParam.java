package com.rongke.web.payutil.wxpay.api.param;

import com.rongke.web.payutil.wxpay.config.WXpayConfig;
import com.rongke.web.payutil.wxpay.util.WXpayCore;
import com.rongke.web.payutil.wxpay.util.WXpayXmlUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一下单 接口参数处理类
 * @author eagle
 * @since 2015年7月7日
 * @version 1.1.0
 */

/**
 *title & description
 *@author eagle
 *@since 2015年7月8日
 *@version 1.1.0 
 */
public class UnifiedOrderParam {

    /**
     * 微信统一下单接口( 预支付接口 )参数构造,包括签名
     * @param params 请求参数<br />
     *            -- appid 公众账号ID 未输入时默认 WXpayConfig.appid <br/>
     *            -- mch_id 商户号 未输入时默认 WXpayConfig.mch_id <br/>
     *            -- device_info 设备号 <br/>
     *            -- body 商品描述 **必输** <br/>
     *            -- detail 商品详情 <br/>
     *            -- attach 附加数据 <br/>
     *            -- out_trade_no 商户订单号 **必输** <br/>
     *            -- fee_type 货币类型 <br/>
     *            -- total_fee 总金额 **必输** 注意 ，为货币最小单位，比如人民币时，单位为分<br/>
     *            -- spbill_create_ip 终端IP **必输** <br/>
     *            -- time_start 交易起始时间 <br/>
     *            -- time_expire 交易结束时间 <br/>
     *            -- goods_tag 商品标记 <br/>
     *            -- notify_url 通知地址 **必输** <br/>
     *            -- trade_type 交易类型 **必输** JSAPI，NATIVE，APP，WAP, <br/>
     *            -- product_id 商品ID <br/>
     *            -- openid 用户标识 <br/>
     * @return 响应结果字符串
     */

    public static String makeRequest(Map<String, String> params) {

        // 处理一些默认参数 商户id，appid， 生成32位随机字符串等
        String appid = (String) params.get("appid");
        if (null == appid || appid.trim().length() == 0) {
            appid = WXpayConfig.appid;
            params.put("appid", appid);
        }
        String mch_id = (String) params.get("mch_id");
        if (null == mch_id || mch_id.trim().length() == 0) {
            mch_id = WXpayConfig.mch_id;
            params.put("mch_id", mch_id);
        }
        String trade_type = (String) params.get("trade_type");

        if (null == trade_type || trade_type.trim().length() == 0) {
            trade_type = WXpayConfig.DEFAULT_TRADE_TYPE;
            params.put("trade_type", trade_type);
        }
        params.put("nonce_str", WXpayCore.getRandomStringByLength(32)); // 生成随机串

        // 附加签名
        Map<String, String> signedMap = WXpayCore.signMap(params, WXpayConfig.api_key);

        // 转换成XML字符串
        String ret = WXpayXmlUtils.map2xml(signedMap);
        return ret;
    }

    /**
     * 使用统一下单接口 (预支付) 成功返回的预支付id等信息拼接真正支付的参数，并签名
     * @param unifiedorderRetMap 预支付接口成功返回的参数
     * @return 用于发起真正支付的参数，已签名
     */
    public static Map<String, String> makePaymentMap(Map<String, String> unifiedorderRetMap) {
        Map<String, String> pmtMap = new HashMap<String, String>();
        pmtMap.put("appid", unifiedorderRetMap.get("appid")); // 公众账号ID
        pmtMap.put("partnerid", unifiedorderRetMap.get("mch_id")); // 商户号
        pmtMap.put("prepayid", unifiedorderRetMap.get("prepay_id")); // 预支付交易会话ID
        pmtMap.put("package", WXpayConfig.PACKAGE); // 扩展字段,目前固定为 "Sign=WXPay"
        pmtMap.put("noncestr", WXpayCore.getRandomStringByLength(32)); // 随机字符串
        // 时间戳 东八区，自1970年1月1日 0点0分0秒以来的秒数
        pmtMap.put("timestamp", ((new Date().getTime()) / 1000) + "");
        return WXpayCore.signMap(pmtMap, WXpayConfig.api_key);
    }

}
