package com.rongke.web.apix;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.antgroup.zmxy.openplatform.api.DefaultZhimaClient;
import com.antgroup.zmxy.openplatform.api.ZhimaApiException;
import com.antgroup.zmxy.openplatform.api.request.ZhimaAuthInfoAuthorizeRequest;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCreditScoreGetRequest;
import com.antgroup.zmxy.openplatform.api.response.ZhimaCreditScoreGetResponse;
import jodd.servlet.URLDecoder;

import java.util.HashMap;
import java.util.Map;

/**
 * @芝麻信用授权
 */

public class ZhimaXinYongtAuth {
    //芝麻开放平台地址";
    private static String gatewayUrl = "https://zmopenapi.zmxy.com.cn/openapi.do";

    private static String appId = "";

    private static String privateKey = "";

    private static String zhimaPublicKey = "";

    public static String zhimaxinyong(String identityParam, String bizParams) {
        ZhimaAuthInfoAuthorizeRequest req = new ZhimaAuthInfoAuthorizeRequest();
        req.setChannel("apppc");
        req.setPlatform("zmop");
        req.setIdentityType("2");// 必要参数

        req.setIdentityParam(identityParam);//"{\"name\":\"张三\",\"certType\":\"IDENTITY_CARD\",\"certNo\":\"141422********\"}" 必要参数
        req.setBizParams(bizParams);//"{\"auth_code\":\"M_H5\",\"channelType\":\"app\",\"state\":\"商户自定义\"}"

        DefaultZhimaClient client = new DefaultZhimaClient(gatewayUrl, appId, privateKey, zhimaPublicKey);
        try {
            String url = client.generatePageRedirectInvokeUrl(req);
            System.out.println(url);
            return url;
        } catch (ZhimaApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 芝麻授权返回参数解析
     *
     * @param params
     * @param sign
     * @return
     */

    public static String getResult(String params, String sign) {
        //String params = "Srnlsy8O0SRZ1wYkFBQQPwLh66o0tscIsL7tAEQz%2Fk%2BNW7QeMSKzDFNmh2B%2Fbaq6GnBEXkQTj4PZqLYBQjBvlCRkgRkSx4g%2FP4lJ6JW2UjAQWTN0djSi28NDyQQSP772YZiLtcw%2Fp%2FHmDb2Uhr24Jp2gPxfdzV9gGDl0uY1gNmYxqfG9tsqlNhnL%2FqTZU4KB%2FHt%2Fz3vaogxAZzo3YiB%2Fj47t97%2FUkoNzk2bWvtV12d275dwgYg31MVMKOUc%2BG%2ByRQsZMsDBKoZfhqFoADnuD0i0a%2FguFZcppxxZKCVIpveT29JtkHFXfYC8F2jZaRH5zXQa1odi%2BRv%2Bt%2BF08O1NrnA%3D%3D";
        //从回调URL中获取params参数，此处为示例值

        //String sign = "BvAYHmZ29xkuIJoNPomGJCUWUDuSufHOPTTlXpzYtY4e3i9mL6QpwPUDuK0d0F9yPxaHBAVq8YEveNoBPnsryFPE1rL3810a5pcHb8WZb02RVqCbc5j5La3SBdHY1fZRUlQYKPY0KwkipkTZmOXZAHIPU3hgynt1m%2BsAscxonIk%3D";
        //从回调URL中获取sign参数，此处为示例值
        //
        //判断串中是否有%，有则需要decode
        if (params.indexOf("%") != -1) {
            params = URLDecoder.decode(params, "UTF-8");
        }
        if (sign.indexOf("%") != -1) {
            sign = URLDecoder.decode(sign, "UTF-8");
        }
        DefaultZhimaClient client = new DefaultZhimaClient(gatewayUrl, appId, privateKey, zhimaPublicKey);
        try {
            String result = client.decryptAndVerifySign(params, sign);
            System.out.println("result:" + result);
            return result;
        } catch (ZhimaApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 发起芝麻分请求
     */
    public static String getScore(String openId, String transactionId) {
        ZhimaCreditScoreGetRequest req = new ZhimaCreditScoreGetRequest();
        /*String transactionId = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
                + UUID.randomUUID().toString();*/ //创建业务流水凭证，以当前时间作为前缀，uuid为后缀
        req.setTransactionId(transactionId); // 必要参数，业务流水凭证
        req.setProductCode("w1010100100000000001"); // 必要参数，这个值对于芝麻分产品是固定的，无需修改
        req.setOpenId(openId); // 必要参数，授权获得的openid
        DefaultZhimaClient client = new DefaultZhimaClient(gatewayUrl, appId, privateKey,
                zhimaPublicKey);
        try {
            ZhimaCreditScoreGetResponse response = client.execute(req);
            // TODO 将业务流水凭证与响应内容持久化到DB，便于后续对账
            System.out.println("transactionId=" + transactionId + ";请求完整响应=" + response.getBody());
            if (response.isSuccess()) {
                //打印正常响应
                System.out.println("用户芝麻信用评分=" + response.getZmScore());
                return response.getZmScore();
            } else {
                //打印异常原因
                System.out.println(response.getErrorCode());
                System.out.println(response.getErrorMessage());
                return null;
            }
        } catch (ZhimaApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws AlipayApiException {
        Map<String, Object> idparms = new HashMap<>();
        idparms.put("name", "汪航");
        idparms.put("certType", "IDENTITY_CARD");
        idparms.put("certNo", "321082199503261396");
        String identityParam = JSON.toJSONString(idparms);

        Map<String, Object> bizMap = new HashMap<>();
        bizMap.put("auth_code", "M_H5");
        bizMap.put("channelType", "APP");
        bizMap.put("state", "113");
        String bizParams = JSON.toJSONString(bizMap);

        System.out.println(bizParams);
        String s = zhimaxinyong(identityParam, bizParams);
        System.out.println(s);
        //getScore("268815281956202772125817055");
        // getResult(a,b);

    }
}





