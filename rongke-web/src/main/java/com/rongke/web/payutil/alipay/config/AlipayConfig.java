package com.rongke.web.payutil.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.util.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088002120964787";
	public static String seller_email = "fushourenjia@qq.com";
	// 商户的私钥 RSA2
	public static String merchant_private_key ="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDTVUOJu5KLhyrm\n" +
			"mgpW2/2McjBrGaBjqyFMeHST70skrq2p0micpv0knl3RxC2q4NmUXOIxC/hPAm04\n" +
			"bFYfreyoq0ci9MaS2nUiAX8vT+UHMmc9rZ4CMDn2DiRDNhEPP95uG14AHUu1rrCC\n" +
			"QqUg/AKE3QxC411o4T2pwrAAiutrLiWSBU/Hx9cxqGgBOozecidsDgoDMk+RkP9J\n" +
			"DnOnXtaY3Wa6fIs2Efn6pFbl1pYp0OjDqbr8tqhTzHHWx26MRRvg5bsD64fiY3TC\n" +
			"rWcM2qo650r+XewheEFGrn9EQtX+DxUNdxveAmAk7ivPHHu7Dpw4R0UZTHDqHh3B\n" +
			"cgqCqeRtAgMBAAECggEBAKseXSOHWC/WcOxsSnbuhq1UQNRX2cEabkoHNw4aMtQv\n" +
			"BLt/felqZYtjGECkAv5cNpGlbBmewZ7oLhwDzPC12ezxUxaA8NE0RqgUYjGjFaXw\n" +
			"MByHGmxXNHeCF+a8PrAEXUz2p/SbxBQGsTGoWSEOLdP7UNRQWfB0gYOV+wFVey46\n" +
			"pK01QHCX9iszSlBUI5qLRAtMmTF8zrLNukvHdpo3Cyfet3UkM0cbbGGrnaU7EyAm\n" +
			"4/JC95ewFsTFif/L3eDuonmv3X/hKtuVyGghj6XPTvYLli1qqExij3kyyi4lP2mT\n" +
			"2QkApKJUKnXVB/nYu7X0vv3vAwwf2/Z5lVuqwNi8jIECgYEA7ugFtDP+a0R/BZ+f\n" +
			"JJTB5l2+vM/WzlPzx0ONiKWmlbSzWUv81hLTdRIUWWJN+03Ziv2xUQ2z0uGPY+E6\n" +
			"mwGT294SZxDClgkb1DxJ0N9UpjCSvuw97sK8qcwQIFCVYjxzJWTiq7SK42GX9KJX\n" +
			"1HJ/ng2Y7eEHZKCv8yu6HeM+87ECgYEA4nQwnQtqN69AL4u/uWEIiKdW34b60824\n" +
			"U0833k+fUMSf18mUv6Xomlxy7sbT1Nv50QbwIt7LMIDasgXsjJ9Y2E8TKljrAiGt\n" +
			"kloX8c085ZLt28pWTT5qkMFXAUV6p9p8ddMVGc3wQwIIx1ibXshFkHE4XY2kycxh\n" +
			"Tk6DVB+IF30CgYEAhs49ibVc9kIsOowbTJTz5H05R2kvmDUe9W1Uy6pNGWRhXx3V\n" +
			"iSYOU5zkBpvFzOvcuOT1zPqiYXgNZRqN6zE0sXfZgnaelfKF1Xg/XrDmHvCBnWHI\n" +
			"gJU5x8Xxw4o1AL2Da7JFhQXW6P8Es3F3hdUl1ZVumavEdC+HmPsvhfXYa8ECgYEA\n" +
			"zX5gavJj1dorZbnsJj3+b3ti4v5q+O7SmI7nhM4+JR34aunbGLpmREUmarV0ntFr\n" +
			"dhpUqWABhKc5TZkAmm8zQT8ohiIgSFPlXUxyNBNpcm6NeAi5nejI7Uj1OgshjdY/\n" +
			"YPBGUp9Fqpja/ILUowMb1UJYwtR5OEJF/rhShnECn0UCgYBzf0Kke1uqH6B0IWHt\n" +
			"uZrt8S89vJY7NXz5Lgs1otgoPWcLnqoPaG2mcmdGpRevNbflEFfD53jVb/pNgfEN\n" +
			"iXIIvd16NVwJ3qHxw50oJ7MRsUIQkvkSbNEYqUF6xeM0bysohDJr0zciyETuKDZB\n" +
			"UKopaLGXhaa9Hk7xSiHZhLSaNw==";

    //RSA2支付宝公钥
	public static String alipay_public_key ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkmeZi62HEZaTGc5O0k6LmTNpvMvy0IbGjMeT769Ieuj8FaC2vTCsu650LORj+6RDVB/g4Mbn269pqz3vEe38u7y2c44wpPxOcT64CbtkKaQNezohkPvJ37xO5U+g0ndZHn6eqOHmWYkYUFfs9m/pM/gxJ9ksouAFHykRVArZ34hTwaDA621nJBzK1q0wkANC9pTQlAxyyrnSsTYsHFYvPIdyvpWv9YHunGxQZbAgEW2v+sV6TrF8q0Vt/SEoo6xIxZAG7G2foU8PU8d/zfzJ4hcvKSh6w9wgQkPJJiIFibtAHPYUOTGY5639GJF2Ml3GSYLlklYC6WDemRZlNkOTNwIDAQAB";

	//RSA
	public static String ali_private_key="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL8DOEC6k5YVyeMs\n" +
			"KM9nWOqzfhniOlBYejC2+veZ+Kt6rhShGSxAEVjxXP28xn/A3a+qVTqmNMFdLOVn\n" +
			"M5MUqnuyCOPFG5xpvT2WcQafMyzlVqgRyt05MLJHlElXXhf2yqyFRrsHTRsA9Lrg\n" +
			"qwsLskThWoQfVhXbKs31OFHXVV9TAgMBAAECgYAip4DjLXZtOlLAZliZGFTnb90m\n" +
			"8QZHYWX5xJjPpx2Ybs96aqfYgV8kVdXbKw6QSvttTWfdFh5oHnFwxUJ0IsGzEvkR\n" +
			"79l7kTEOPspzLewEa8bEdFRCdyUaqTlWHIZ3+WM/VJtrEIPXB79LPnU9ADw16M4G\n" +
			"k7v4aBQphq2SvS9msQJBAOusarTpeL7Zcj1Ifa1qyO74sZqbYiNuzJHwZ/0S1eMD\n" +
			"0VORNNSxh6P7joXjPsf7MWIRGkb14B+XLE7gY9z/M6UCQQDPfLTQC//edsZmRu/a\n" +
			"avCa9xWIQ/X+upS/Pi7+0a9ew3BcqxE+jy8J/K7WAwTbyCsdtgcRSowG1fRaRfxN\n" +
			"DfWXAkBb+rziwsRXaDKH7ITwa97kNbfLaZ2RyqqYwsUzJmjH4zONc/y0jl0sCQAF\n" +
			"DT3xev45Ege5bjj821l1p2oSVJ/pAkBqzOAtbt/H3Q+GueDgZsdunNyChajr85J5\n" +
			"VkLOe5RvjN4bdRqtPvqRg3VjR66KiuBWrMm6hsrJAfyDMx8JcrQFAkEAtYIU6DRU\n" +
			"EECeKXtUWhSdePXQFBimQ+V6sasum+EC+PP+rNGHtQRIuGBoQT/TEQTZbHlYV3z9\n" +
			"1GiROQTTTb8lww==";

	// 支付宝的公钥，无需修改该值 RSA
	public static String ali_public_key  ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	public static String input_charset1 = "gbk";
	// 签名方式 不需修改
	public static String sign_type = "RSA2";
	//public static String sign_type1 = "RSA";

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2018030102295371";
	// 仅支持JSON
	public static String format = "json";

	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";

	// 支付宝网关
	//public static String log_path = "C:\\";
}
