package com.rongke.web.payutil.helipay.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongke.model.LoanOrder;
import com.rongke.model.UserBank;
import com.rongke.web.payutil.helipay.util.HttpClientService;
import com.rongke.web.payutil.helipay.util.MessageHandle;
import com.rongke.web.payutil.helipay.vo.response.TransferQueryResponVo;
import com.rongke.web.payutil.helipay.vo.response.TransferResponVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.rongke.web.payutil.helipay.action.QuickPayApi.isJSON;

/**
 * 代付api
 * @Company 杭州木瓜科技有限公司
 * @className: com.rongke.web.payutil.helipay.action
 * @author songlan@dianjia.com 
 * @date 2018/10/11. 11:25 PM
 */
public class TransferApi {
	private final static Log log = LogFactory.getLog(TransferApi.class);

	/**
	 * 商户号
	 */
	public static final String MERCHANT_NO = "C1800368599";
	/**
	 * 代付接口地址
	 */
    public static final String REQUEST_URL_TRANSFER = "http://pay.trx.helipay.com/trx/transfer/interface.action";
	/**
	 * 代付回调地址
	 */
	public static final String TRANSFER_NOTIFY_URL = "http://api.fintech-sx.com:8080/api/money/transferNotify";

	/**
	 * 合利宝代付
	 * @param userBank
	 * @param loanOrder
	 * @return
	 */
	public static Map<String, Object> transfer(UserBank userBank, LoanOrder loanOrder) {
		log.info("<-- 合利宝代付开始 -->");
		Map<String, Object> resultMap ;
		try {
			Map<String,String> requestMap = new LinkedHashMap<>();
			requestMap.put("P1_bizType","Transfer");
			requestMap.put("P2_orderId",loanOrder.getId().toString()+"2");
			requestMap.put("P3_customerNumber", TransferApi.MERCHANT_NO);
			requestMap.put("P4_amount",loanOrder.getRealMoney().toString());
			requestMap.put("P5_bankCode",userBank.getCardtype());
			requestMap.put("P6_bankAccountNo",userBank.getBankcardno());
			requestMap.put("P7_bankAccountName",userBank.getName());
			requestMap.put("P8_biz","B2C");
			requestMap.put("P9_bankUnionCode","");
			requestMap.put("P10_feeType","PAYER");
			requestMap.put("P11_urgency","true");
			requestMap.put("P12_summary","");
			log.info("合利宝代付请求：" + requestMap);
			String reqStr  =  requestMap.entrySet().stream().map(entry->"&"+entry.getValue()).collect(Collectors.joining());
			String sign =   MessageHandle.sign(reqStr);
            requestMap.put("notifyUrl", TRANSFER_NOTIFY_URL);
            requestMap.put("sign",sign);
			resultMap = HttpClientService.getHttpResp(requestMap, REQUEST_URL_TRANSFER);
			log.info("合利宝代付响应结果：" + resultMap);

			if ( resultMap ==  null || !"0000".equals(resultMap.get("rt2_retCode"))) {
				log.error("请求代付失败,响应结果：" + resultMap);
			}

			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				resultMap.put("rt2_retCode", "0001");
				resultMap.put("rt3_retMsg", "请求失败");
				return resultMap;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				resultMap.put("rt2_retCode", "0001");
				resultMap.put("rt3_retMsg", resultMsg);
				return resultMap;
			}

			TransferResponVo responseVo = JSONObject.parseObject(resultMsg, TransferResponVo.class);
//				if (!MessageHandle.checkSign(responseVo)) {
//					json.put("rt2_retCode", "0001");
//					json.put("rt3_retMsg", "验签失败");
//					return json;
//				}
			return JSON.parseObject(JSONObject.toJSONString(responseVo));
		} catch (Exception e) {
			log.error("transfer 异常：" + e.getMessage(), e);
			resultMap = new HashMap<>();
			resultMap.put("rt2_retCode", "01");
			resultMap.put("rt3_retMsg", "请求异常：" + e.getMessage());
		}
		return resultMap;
	}

	/**
	 * 代付账单结果 查询
	 * @param orderId
	 * @return
	 */
	public static Map<String, Object> transferSingleQuery(String orderId){
		log.info("transferSingleQuery 代付查询 账单号：" + orderId);
		Map<String, Object> resultMap = new HashMap<>();
		try {
			Map<String,String> requestMap = new HashMap<>();
			requestMap.put("P1_bizType", "TransferQuery");
			requestMap.put("P2_orderId", orderId);
			requestMap.put("P3_customerNumber", MERCHANT_NO);

			String reqStr  =  requestMap.entrySet().stream().map(entry->"&"+entry.getValue()).collect(Collectors.joining());
			String sign =   MessageHandle.sign(reqStr);
			requestMap.put("sign",sign);
			//发起请求
			resultMap = HttpClientService.getHttpResp(requestMap, REQUEST_URL_TRANSFER);
			log.info("transferSingleQuery 代付查询 响应结果：" + resultMap);

			if (resultMap ==  null) {
				resultMap = new HashMap<>();
				resultMap.put("rt2_retCode", "0001");
				resultMap.put("rt3_retMsg", "请求失败");
				return resultMap;
			}
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				resultMap.put("rt2_retCode", "0001");
				resultMap.put("rt3_retMsg", "请求失败");
				return resultMap;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				resultMap.put("rt2_retCode", "0001");
				resultMap.put("rt3_retMsg", resultMsg);
				return resultMap;
			}

			TransferQueryResponVo responseVo = JSONObject.parseObject(resultMsg, TransferQueryResponVo.class);
			return JSON.parseObject(JSONObject.toJSONString(responseVo));
		} catch (Exception e) {
			log.error("transferSingleQuery error", e);
			resultMap = new HashMap<>();
			resultMap.put("rt2_retCode", "01");
			resultMap.put("rt3_retMsg", "请求异常：" + e.getMessage());
		}
		return resultMap;
	}
}
