package com.rongke.web.yibaopay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.rongke.utils.OrderUtils;
import com.rongke.web.yibaopay.model.FirstPaymentBean;
import com.rongke.yibaoUtils.YiBaoUtils;
import com.yeepay.g3.facade.yop.ca.dto.DigitalEnvelopeDTO;
import com.yeepay.g3.facade.yop.ca.enums.CertTypeEnum;
import com.yeepay.g3.sdk.yop.utils.InternalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

@Component
public class YbApiHandler {

    private static  YbParamConfig ybParamConfig;

    private static final String ybOrderIdPrefix ="RKHK02";

    @Autowired
    public void setYbParamConfig(YbParamConfig ybParamConfig) {
        YbApiHandler.ybParamConfig = ybParamConfig;
    }



    public static Map<String, String> decodeParams(HttpServletRequest req){
        //开始解密
        String response = req.getParameter("response");
        Map<String,String> jsonMap  = new HashMap<>();
        DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
        dto.setCipherText(response);
        //InternalConfig internalConfig = InternalConfig.Factory.getInternalConfig();
        PrivateKey privateKey = InternalConfig.getISVPrivateKey(CertTypeEnum.RSA2048);
        PublicKey publicKey = InternalConfig.getYopPublicKey(CertTypeEnum.RSA2048);

        dto = DigitalEnvelopeUtils.decrypt(dto, privateKey, publicKey);
        System.out.println("解密结果:"+dto.getPlainText());
        return jsonMap = parseResponse(dto.getPlainText());
    }

    /**
     * 强制支付接口
     * @param
     * @return
     * @throws IOException
     */
    public static Map<String, String> constraintPayment(String phone,String bankCardNo,String amount,Long orderId) throws IOException {
        String url =ybParamConfig.getUnifiedPayUrl();
        Map<String, String> map = new HashMap();
        map.put("requestno",orderId+OrderUtils.getYdOrderNo());
        map.put("issms","false");
        map.put("identityid",phone);
        map.put("identitytype", "PHONE");
        map.put("cardtop",bankCardNo.substring(0,6));
        map.put("cardlast",bankCardNo.substring(bankCardNo.length() - 4, bankCardNo.length()));
        map.put("amount",amount);
        map.put("terminalno",ybParamConfig.getTerminalno());
        String unifiedPayCallBackUrl = ybParamConfig.getUnifiedPayCallBackUrl();
        boolean equals = "http://dfjtv6.natappfree.cc/api/money/constraintPaymentCallback".equals(unifiedPayCallBackUrl);
        map.put("callbackurl", ybParamConfig.getUnifiedPayCallBackUrl());
        map.put("requesttime", YiBaoUtils.getSystemDate());
        map.put("productname","HKZF");

        Map<String, String> yopresponsemap = YeepayService.yeepayYOP(map, url);
        return yopresponsemap;
    }


    /**
     * 首次支付接口
     *
     * @param firstPaymentBean
     * @return
     */
    public static Map<String, String> firstPayment(FirstPaymentBean firstPaymentBean){
        String url =ybParamConfig.getFirstPaymentUrl();
        String amount = firstPaymentBean.getAmount();
        amount = "1"; //测试强制扣除1分钱

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("requestno",ybOrderIdPrefix+OrderUtils.getYdOrderNo());
        paramMap.put("identityid",firstPaymentBean.getPhone());
        paramMap.put("identitytype", "PHONE");
        paramMap.put("cardno",firstPaymentBean.getBankCardNo());
        paramMap.put("idcardno",firstPaymentBean.getIdcardNo());
        paramMap.put("idcardtype","ID");
        paramMap.put("username",firstPaymentBean.getRealName());
        paramMap.put("phone",firstPaymentBean.getPhone());
        paramMap.put("amount",amount);
        paramMap.put("terminalno",ybParamConfig.getTerminalno());
        paramMap.put("authtype","COMMON_FOUR");

        paramMap.put("issms","true");
        paramMap.put("callbackurl",ybParamConfig.getFirstPaymentCallBackUrl());
        paramMap.put("requesttime",YiBaoUtils.getSystemDate());
        paramMap.put("productname", firstPaymentBean.getProductName());
        Map<String, String> resultMap = YeepayService.yeepayYOP(paramMap,url);
        return resultMap;
    }

    /**
     * 首次支付 短信确认接口
     * @param requestno
     * @param validatecode
     * @return
     */
    public static Map<String, String> firstPaymentConfirm(String requestno,String validatecode){
        String url =ybParamConfig.getFirstPaymentConfirmUrl();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("requestno",requestno);
        paramMap.put("validatecode",validatecode);

        Map<String, String> resultMap = YeepayService.yeepayYOP(paramMap,url);
        return resultMap;
    }

    public static Map<String, String> parseResponse(String response){

        Map<String,String> jsonMap  = new HashMap<>();
        jsonMap	= JSON.parseObject(response,
                new TypeReference<Map<String,String>>() {});

        return jsonMap;
    }
}
