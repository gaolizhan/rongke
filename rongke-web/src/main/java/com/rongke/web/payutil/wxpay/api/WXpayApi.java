package com.rongke.web.payutil.wxpay.api;

import com.rongke.web.payutil.wxpay.api.param.OrderQueryParam;
import com.rongke.web.payutil.wxpay.api.param.UnifiedOrderParam;
import com.rongke.web.payutil.wxpay.config.WXpayConfig;
import com.rongke.web.payutil.wxpay.util.WXpayCore;

import java.util.Map;

public class WXpayApi {

    /**
     * 微信统一下单接口( 预支付接口 ) API
     * @param params 请求参数<br />
     *            -- appid 公众账号ID 未输入时默认 WXpayConfig.appid <br/>
     *            -- mch_id 商户号 未输入时默认 WXpayConfig.mch_id <br/>
     *            -- device_info 设备号 <br/>
     *            -- body 商品描述 **必输** <br/>
     *            -- detail 商品详情 <br/>
     *            -- attach 附加数据 <br/>
     *            -- out_trade_no 商户订单号 **必输** 注意 ，为货币最小单位，比如人民币时，单位为分<br/>
     *            -- fee_type 货币类型 <br/>
     *            -- total_fee 总金额 **必输** <br/>
     *            -- spbill_create_ip 终端IP **必输** <br/>
     *            -- time_start 交易起始时间 <br/>
     *            -- time_expire 交易结束时间 <br/>
     *            -- goods_tag 商品标记 <br/>
     *            -- notify_url 通知地址 **必输** <br/>
     *            -- trade_type 交易类型 **必输** JSAPI，NATIVE，APP，WAP, <br/>
     *            -- product_id 商品ID <br/>
     *            -- openid 用户标识 <br/>
     * @return 响应结果字符串
     * @see UnifiedOrderParam#makeRequest(Map)
     */
    public static String unifiedOrder(Map<String, String> params) {

        String reqStr = UnifiedOrderParam.makeRequest(params);
        return WXpayCore.postXml(WXpayConfig.unifiedorder_url, reqStr);
    }

    /**
     * 微信统一下单接口( 预支付接口 ) API， 结果以map方式返回
     * @param params 请求参数<br />
     * @return 响应结果(Map形式)
     * @see WXpayApi#unifiedOrder(Map)
     */
    public static Map<String, String> unifiedOrderRetMap(Map<String, String> params) {
        return WXpayCore.getRetMap(unifiedOrder(params));
    }

    /**
     * 将 统一下单接口(预支付) 成功返回的数据 拼接再签名后发给App,发起真正支付使用 准备发起支付的参数，返回给客户端App使用，
     * @param unifiedorderRetMap 统一支付成功返回的结果
     * @return
     */
    public static Map<String, String> makePaymentMap(Map<String, String> unifiedorderRetMap) {
        return UnifiedOrderParam.makePaymentMap(unifiedorderRetMap);
    }

    /**
     * 订单查询接口 API
     * @param params 请求参数<br />
     *            -- appid 公众账号ID 未输入时默认 WXpayConfig.appid <br/>
     *            -- mch_id 商户号 未输入时默认 WXpayConfig.mch_id <br/>
     *            -- transaction_id 微信订单号 <br/>
     *            -- out_trade_no 商户订单号 **没提供transaction_id时需要传这个** <br/>
     * @return 响应结果字符串
     * @see OrderQueryParam#makeRequest(Map)
     */
    public static String orderQuery(Map<String, String> params) {
        String reqStr = OrderQueryParam.makeRequest(params);
        return WXpayCore.postXml(WXpayConfig.orderquery_url, reqStr);
    }

    /**
     * 订单查询接口 API， 结果以map方式返回
     * @param params 请求参数<br />
     * @return 响应结果(Map形式)
     * @see WXpayApi#orderQuery(Map)
     */
    public static Map<String, String> orderQueryRetMap(Map<String, String> params) {
        return WXpayCore.getRetMap(orderQuery(params));
    }

}
