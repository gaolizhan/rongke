package com.rongke.web.payutil.wxpay.api.param;



import com.rongke.web.payutil.wxpay.config.WXpayConfig;
import com.rongke.web.payutil.wxpay.util.WXpayCore;
import com.rongke.web.payutil.wxpay.util.WXpayXmlUtils;

import java.util.Map;

/**
 * 订单查询 接口参数处理类
 * @author eagle
 * @since 2015年7月7日
 * @version 1.1.0
 */

public class OrderQueryParam {

    /**
     * 订单查询接口 参数构造,包括签名
     * @param params 请求参数<br />
     *            -- appid 公众账号ID 未输入时默认 WXpayConfig.appid <br/>
     *            -- mch_id 商户号 未输入时默认 WXpayConfig.mch_id <br/>
     *            -- transaction_id 微信订单号 <br/>
     *            -- out_trade_no 商户订单号 **没提供transaction_id时需要传这个** <br/>
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
        params.put("nonce_str", WXpayCore.getRandomStringByLength(32)); // 生成随机串

        // 附加签名
        Map<String, String> signedMap = WXpayCore.signMap(params, WXpayConfig.api_key);

        // 转换成XML字符串
        String ret = WXpayXmlUtils.map2xml(signedMap);
        return ret;
    }

}
