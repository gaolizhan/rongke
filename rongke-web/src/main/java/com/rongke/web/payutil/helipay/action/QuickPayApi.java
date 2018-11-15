package com.rongke.web.payutil.helipay.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rongke.web.payutil.helipay.util.HttpClientService;
import com.rongke.web.payutil.helipay.util.MessageHandle;
import com.rongke.web.payutil.helipay.vo.request.AgreementBindCardValidateCodeVo;
import com.rongke.web.payutil.helipay.vo.request.BindCardPayVo;
import com.rongke.web.payutil.helipay.vo.request.BindPayValidateCodeVo;
import com.rongke.web.payutil.helipay.vo.request.ConfirmPayVo;
import com.rongke.web.payutil.helipay.vo.request.CreateOrderVo;
import com.rongke.web.payutil.helipay.vo.request.QueryOrderVo;
import com.rongke.web.payutil.helipay.vo.request.QuickPayBindCardVo;
import com.rongke.web.payutil.helipay.vo.request.SendValidateCodeVo;
import com.rongke.web.payutil.helipay.vo.response.AgreementSendValidateCodeResponseVo;
import com.rongke.web.payutil.helipay.vo.response.BindCardPayResponseVo;
import com.rongke.web.payutil.helipay.vo.response.BindCardResponseVo;
import com.rongke.web.payutil.helipay.vo.response.BindPayValidateCodeResponseVo;
import com.rongke.web.payutil.helipay.vo.response.ConfirmPayResponseVo;
import com.rongke.web.payutil.helipay.vo.response.QueryOrderResponseVo;
import com.rongke.web.payutil.helipay.vo.response.SendValidateCodeResponseVo;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class QuickPayApi{

	private final static Log log = LogFactory.getLog(QuickPayApi.class);

	public static final String  HELIPAY_NOTIFY_URL = "http://api.fintech-sx.com:8080/api/money/receivePayResult";

	public static final String MERCHANT_NO = "C1800368599";
	public static final String REQUEST_URL_TRANSFER = "http://pay.trx.helipay.com/trx/transfer/interface.action";
	public static final String REQUEST_URL_QUICKPAY = "http://pay.trx.helipay.com/trx/quickPayApi/interface.action";
	private CreateOrderVo requestVo;
	private Model model;
	//	public static final String REQUEST_URL_QUICKPAY = "http://pay.trx.helipay.com/trx/quickPayApi/interface.action";

//	@RequestMapping(value = "/createOrder")
//	public ModelAndView createOrder(CreateOrderVo requestVo, Model model) {
//		this.requestVo = requestVo;
//		this.model = model;
//		log.info("--------进入创建订单接口----------");
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("quickPayApi/createOrder");
//		try {
//			Map reqestMap = MessageHandle.getReqestMap(requestVo);
//			log.info("请求参数：" + reqestMap);
//			Map<String, Object> resultMap = HttpClientService
//					.getHttpResp(reqestMap, REQUEST_URL_QUICKPAY);
//			log.info("响应结果：" + resultMap);
//
//			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
//				mav.addObject("message", "请求失败");
//				return mav;
//			}
//
//			String resultMsg = (String) resultMap.get("response");
//			if (!isJSON(resultMsg)) {
//				mav.addObject("message", resultMsg);
//				return mav;
//			}
//
//			CreateOrderResponseVo responseVo = JSONObject.parseObject(resultMsg, CreateOrderResponseVo.class);
//			if (!MessageHandle.checkSign(responseVo)) {
//				mav.addObject("message", "验签失败");
//				return mav;
//			}
//
//			if (!"0000".equals(responseVo.getRt2_retCode())) {
//				mav.addObject("message", responseVo.getRt3_retMsg());
//				return mav;
//			}
//
//			mav.setViewName("quickPayApi/nextStep");
//			mav.addObject("P2_customerNumber", responseVo.getRt4_customerNumber());
//			mav.addObject("P3_orderId", responseVo.getRt5_orderId());
//			mav.addObject("P5_phone", requestVo.getP13_phone());
//
//		} catch (Exception e) {
//			log.info("交易异常：" + e.getMessage(), e);
//			mav.addObject("message", "交易异常：" + e.getMessage());
//		}
//		return mav;
//	}

	public static JSONObject agreementBindCardValidateCode(AgreementBindCardValidateCodeVo requestVo) {
		log.info("--------进入发送短信接口----------");

		requestVo.setP1_bizType("AgreementPayBindCardValidateCode");
		requestVo.setP2_customerNumber(MERCHANT_NO);
		JSONObject json = new JSONObject();
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, REQUEST_URL_QUICKPAY);
			log.info("响应结果：" + resultMap);

			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				json.put("rt2_retCode", "0001");
				json.put("rt3_retMsg", "请求失败");
				return json;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				json.put("rt2_retCode", "0001");
				json.put("rt3_retMsg", resultMsg);
				return json;
			}

			AgreementSendValidateCodeResponseVo responseVo = JSONObject.parseObject(resultMsg, AgreementSendValidateCodeResponseVo.class);
			if (!MessageHandle.checkSign(responseVo)) {
				json.put("rt2_retCode", "0001");
				json.put("rt3_retMsg", "验签失败");
				return json;
			}

			if (!"0000".equals(responseVo.getRt2_retCode())) {
				json.put("rt2_retCode", "0001");
			} else {
				json.put("rt2_retCode", "0001");
			}
			json.put("rt3_retMsg", responseVo.getRt3_retMsg());
			return JSON.parseObject(JSONObject.toJSONString(responseVo));

		} catch (Exception e) {
			log.info("异常：" + e.getMessage(), e);
			json.put("rt2_retCode", "01");
			json.put("rt3_retMsg", "请求异常：" + e.getMessage());
		}
		return json;
	}

	public static JSONObject quickPayBindCard(QuickPayBindCardVo requestVo) {
		log.info("--------进入短信验证接口----------");

		requestVo.setP1_bizType("QuickPayBindCard");
		requestVo.setP2_customerNumber(MERCHANT_NO);
		JSONObject json = new JSONObject();
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, REQUEST_URL_QUICKPAY);
			log.info("响应结果：" + resultMap);

			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				json.put("rt2_retCode", "0001");
				json.put("rt3_retMsg", "请求失败");
				return json;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				json.put("rt2_retCode", "0001");
				json.put("rt3_retMsg", resultMsg);
				return json;
			}

			BindCardResponseVo responseVo = JSONObject.parseObject(resultMsg, BindCardResponseVo.class);
			if (!MessageHandle.checkSign(responseVo)) {
				json.put("rt2_retCode", "0001");
				json.put("rt3_retMsg", "验签失败");
				return json;
			}

			if (!"0000".equals(responseVo.getRt2_retCode())) {
				json.put("rt2_retCode", "0001");
			} else {
				json.put("rt2_retCode", "0001");
			}
			json.put("rt3_retMsg", responseVo.getRt3_retMsg());
			return JSON.parseObject(JSONObject.toJSONString(responseVo));

		} catch (Exception e) {
			log.info("异常：" + e.getMessage(), e);
			json.put("rt2_retCode", "01");
			json.put("rt3_retMsg", "请求异常：" + e.getMessage());
		}
		return json;
	}




	public static JSONObject sendValidateCode(SendValidateCodeVo requestVo) {
		log.info("--------进入发送短信接口----------");
		JSONObject json = new JSONObject();
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, REQUEST_URL_QUICKPAY);
			log.info("响应结果：" + resultMap);

			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				json.put("code", "01");
				json.put("message", "请求失败");
				return json;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				json.put("code", "01");
				json.put("message", resultMsg);
				return json;
			}

			SendValidateCodeResponseVo responseVo = JSONObject.parseObject(resultMsg, SendValidateCodeResponseVo.class);
			if (!MessageHandle.checkSign(responseVo)) {
				json.put("code", "01");
				json.put("message", "验签失败");
				return json;
			}

			if (!"0000".equals(responseVo.getRt2_retCode())) {
				json.put("code", "01");
			} else {
				json.put("code", "00");
			}
			json.put("message", responseVo.getRt3_retMsg());
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", "01");
			json.put("message", "请求异常：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping(value = "/confirmPay")
	public ModelAndView confirmPay(ConfirmPayVo requestVo, Model model) {
		log.info("--------进入确认支付接口----------");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("quickPayApi/response");
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, REQUEST_URL_QUICKPAY);
			log.info("响应结果：" + resultMap);

			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				mav.addObject("message", "请求失败");
				return mav;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				mav.addObject("message", resultMsg);
				return mav;
			}

			ConfirmPayResponseVo responseVo = JSONObject.parseObject(resultMsg, ConfirmPayResponseVo.class);
			if (!MessageHandle.checkSign(responseVo)) {
				mav.addObject("message", "验签失败");
				return mav;
			}

			mav.addObject("json", JSONObject.toJSON(responseVo));

		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("message", "交易异常：" + e.getMessage());
		}
		return mav;
	}

//	@RequestMapping(value = "/bindCard")
//	public ModelAndView bindCard(BindCardVo requestVo) {
//		log.info("--------进入绑卡接口----------");
//		try {
//			Map reqestMap = MessageHandle.getReqestMap(requestVo);
//			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, REQUEST_URL_QUICKPAY);
//			log.info("响应结果：" + resultMap);
//
//			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
//				mav.addObject("message", "请求失败");
//				return mav;
//			}
//
//			String resultMsg = (String) resultMap.get("response");
//			if (!isJSON(resultMsg)) {
//				mav.addObject("message", resultMsg);
//				return mav;
//			}
//
//			BindCardResponseVo responseVo = JSONObject.parseObject(resultMsg, BindCardResponseVo.class);
//			if (!MessageHandle.checkSign(responseVo)) {
//				mav.addObject("message", "验签失败");
//				return mav;
//			}
//
//			mav.addObject("json", JSONObject.toJSON(responseVo));
//		} catch (Exception e) {
//			e.printStackTrace();
//			mav.addObject("message", "交易异常：" + e.getMessage());
//		}
//		return mav;
//	}


	public static JSONObject bindPayValidateCode(BindPayValidateCodeVo requestVo) {
		log.info("--------进入发送短信接口----------");
		requestVo.setP1_bizType("QuickPayBindPayValidateCode");
		requestVo.setP2_customerNumber(MERCHANT_NO);
		JSONObject json = new JSONObject();
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, REQUEST_URL_QUICKPAY);
			log.info("响应结果：" + resultMap);

			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				json.put("rt2_retCode", "01");
				json.put("message", "请求失败");
				return json;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				json.put("rt2_retCode", "01");
				json.put("message", resultMsg);
				return json;
			}

			BindPayValidateCodeResponseVo responseVo = JSONObject.parseObject(resultMsg, BindPayValidateCodeResponseVo.class);
			if (!MessageHandle.checkSign(responseVo)) {
				json.put("rt2_retCode", "01");
				json.put("message", "验签失败");
				return json;
			}

			return JSONObject.parseObject(JSON.toJSONString(responseVo));
		} catch (Exception e) {
			e.printStackTrace();
			json.put("rt2_retCode", "01");
			json.put("message", "请求异常：" + e.getMessage());
		}
		return json;
	}

	public static JSONObject bindCardPay(BindCardPayVo requestVo) {
		log.info("--------进入绑卡支付接口----------");
		requestVo.setP1_bizType("QuickPayBindPay");
		requestVo.setSignatureType("MD5WITHRSA");
		requestVo.setP2_customerNumber(MERCHANT_NO);
		JSONObject json = new JSONObject();
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, REQUEST_URL_QUICKPAY);
			log.info("响应结果：" + resultMap);

			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				json.put("rt2_retCode", "01");
				json.put("rt3_retMsg", "请求失败");
				return json;
			}


			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				json.put("rt2_retCode", "01");
				json.put("rt3_retMsg", resultMsg);
				return json;
			}

			BindCardPayResponseVo responseVo = JSONObject.parseObject(resultMsg, BindCardPayResponseVo.class);
			if (!MessageHandle.checkSign(responseVo)) {
				json.put("rt2_retCode", "01");
				json.put("rt3_retMsg", "验签失败");
				return json;
			}

			return JSONObject.parseObject(JSON.toJSONString(responseVo));
		} catch (Exception e) {
			e.printStackTrace();
			json.put("rt2_retCode", "01");
			json.put("rt3_retMsg", "请求异常：" + e.getMessage());
		}
		return json;
	}

	public static  JSONObject queryOrder(QueryOrderVo requestVo) {
		log.info("--------进入订单查询接口----------");
		requestVo.setP1_bizType("QuickPayQuery");
		requestVo.setP3_customerNumber(MERCHANT_NO);

		JSONObject json = new JSONObject();
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, REQUEST_URL_QUICKPAY);
			log.info("响应结果：" + resultMap);

			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				json.put("rt2_retCode", "01");
				json.put("rt3_retMsg", "请求失败");
				return json;
			}


			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				json.put("rt2_retCode", "01");
				json.put("rt3_retMsg", resultMsg);
				return json;
			}

			QueryOrderResponseVo responseVo = JSONObject.parseObject(resultMsg, QueryOrderResponseVo.class);
			if (!MessageHandle.checkSign(responseVo)) {
				json.put("rt2_retCode", "01");
				json.put("rt3_retMsg", "验签失败");
				return json;
			}

			return JSONObject.parseObject(JSON.toJSONString(responseVo));
		} catch (Exception e) {
			e.printStackTrace();
			json.put("rt2_retCode", "01");
			json.put("rt3_retMsg", "请求异常：" + e.getMessage());
		}
		return json;
	}

	public static boolean isJSON(String test) {
		try {
			JSONObject.parseObject(test);
		} catch (JSONException ex) {
			return false;
		}
		return true;
	}


}
