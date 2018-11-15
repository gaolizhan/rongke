package com.rongke.web.payutil.wxpay.util;

import com.rongke.web.payutil.wxpay.config.WXpayConfig;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信支付接口的xml处理工具
 * 
 * @author eagle
 * @since 2015年7月8日
 * @version 1.1.0
 */
public class WXpayXmlUtils {

	/**
	 * 将Map转为xml字符串
	 * 
	 * @param map
	 * @return
	 */
	public static String map2xml(Map<String, String> map) {
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>\n");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			xml.append("<").append(entry.getKey()).append("><![CDATA[").append(entry.getValue()).append("]]></")
					.append(entry.getKey()).append(">\n");
		}
		xml.append("</xml>");

		String ret = xml.toString();
		return ret;
	}

	/**
	 * 将微信返回的xml字符串转为Map<String,String>
	 * 
	 * @param
	 * @return
	 */
	public static Map<String, String> xml2map(String xml) {
		Map<String,String> map = new HashMap<String,String>();
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			List<Element> list = rootElt.elements();//获取根节点下所有节点
			for (Element element : list) {  //遍历节点
				map.put(element.getName(), element.getText()); //节点的name为map的key，text为map的value
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String[] args) {
		String testXML = "<xml><return_code><![CDATA[SUCCESS]]></return_code>"
				+ "<return_msg><![CDATA[OK]]></return_msg>" + "<appid><![CDATA[wx21922ce19d2a4dc6]]></appid>"
				+ "<mch_id><![CDATA[1249261801]]></mch_id>" + "<device_info><![CDATA[aaa]]></device_info>"
				+ "<nonce_str><![CDATA[HLIoBodUnYiGexPO]]></nonce_str>"
				+ "<sign><![CDATA[6343017C6B23FFA96DCF57E4012C3223]]></sign>"
				+ "<result_code><![CDATA[FAIL]]></result_code>" + "<err_code><![CDATA[OUT_TRADE_NO_USED]]></err_code>"
				+ "<err_code_des><![CDATA[商户订单号重复]]></err_code_des>" + "</xml>";

		Map<String, String> retMap = xml2map(testXML);
		System.out.println(retMap);
		String sign = retMap.get("sign");
		String newSign = WXpayCore.getSign(retMap, WXpayConfig.api_key);
		System.out.println(sign.equals(newSign));
	}
}
