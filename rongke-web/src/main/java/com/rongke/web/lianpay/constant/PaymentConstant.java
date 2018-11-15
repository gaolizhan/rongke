package com.rongke.web.lianpay.constant;

public class PaymentConstant {

	/** 连连银通公钥（不需替换，这是连连公钥，用于报文加密和连连返回响应报文时验签，不是商户生成的公钥. */
	public static final String PUBLIC_KEY_ONLINE = "";

	/** 商户私钥 商户通过openssl工具生成公私钥，公钥通过商户站上传，私钥用于加签，替换下面的值 . */
	public static final String BUSINESS_PRIVATE_KEY = "";

	/** 商户号（商户需要替换自己的商户号）. */
	public static final String OID_PARTNER = "";

	/** 实时付款api版本. */
	public static final String API_VERSION = "1.0";

	/** 实时付款签名方式. */
	public static final String SIGN_TYPE = "RSA";

}
