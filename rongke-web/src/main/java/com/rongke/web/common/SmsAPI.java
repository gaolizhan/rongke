package com.rongke.web.common;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Random;

/**
 * 普通短信发送接口
 */
@Service
@Transactional
public class SmsAPI {

	public static final String ACCESSKEY = "wTefSGfz23B6it87";
	public static final String ACCESSSCRECT = "UJzrrCnCXjhdW34ZliqRbiHVEHvUJDc8";
	public static final String SIGN = "【金元宝】";


	//发送验证码
	public  String sendCode (String phone){
		String code = randomString(6);
		SmsAPI smsAPI = new SmsAPI();
		String content = "您好，您的验证码为"+code+"，请注意保管。";
		smsAPI.sendsms(phone,content);
		return code;
	};


	//发送验证码
	public  String adminSendCode (String phone,String adminName){
		String code = randomString(6);
		SmsAPI smsAPI = new SmsAPI();
		//String content = "您好，您的验证码为"+code+"，请注意保管。";
		String content ="你好，"+adminName+"申请登录，验证码是"+code+"，请注意保管！";
		smsAPI.sendsms(phone,content);
		return code;
	};


	public String MD5(String value) throws Exception  {
        StringBuffer md5StrBuff = new StringBuffer();

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(value.getBytes("UTF-8"));

        byte[] result = md5.digest();

        for (int i = 0; i < result.length; i++)
			if (Integer.toHexString(0xFF & result[i]).length() == 1) {
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & result[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & result[i]));
			}

        return md5StrBuff.toString();
    }

	public void sendsms(String phone,String content) {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod("http://api.1cloudsp.com/api/send");
		postMethod.getParams().setContentCharset("UTF-8");
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		String random = RandomStringUtils.random(10); //����ַ���
		String timestamp = ""+System.currentTimeMillis(); //��ǰʱ���
		try {
			String token = this.MD5(ACCESSSCRECT+random+timestamp); //����token

			NameValuePair[] data = {
                    new NameValuePair("token", token),
                    new NameValuePair("accesskey", ACCESSKEY),
                    new NameValuePair("timestamp", timestamp),
                    new NameValuePair("random", random),
                    new NameValuePair("mobile", phone),
                    new NameValuePair("content", URLEncoder.encode(content, "utf-8")),
                    new NameValuePair("sign",SIGN )

            };
			postMethod.setRequestBody(data);

			int statusCode = httpClient.executeMethod(postMethod);
			System.out.println("statusCode: " + statusCode + ", body: " + postMethod.getResponseBodyAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取count个随机数
	 *
	 * @param count 随机数个数
	 * @return 随机数
	 */
	private static String randomString(int count) {
		StringBuilder sb = new StringBuilder();
		String str = "0123456789";
		Random r = new Random();
		for (int i = 0; i < count; i++) {
			int num = r.nextInt(str.length());
			sb.append(str.charAt(num));
			str = str.replace((str.charAt(num) + ""), "");
		}
		return sb.toString();
	}


	public static void main(String[] args) throws Exception {
		SmsAPI t = new SmsAPI();
		t.sendsms("18375321523","尊敬的xx您好！你已经通过 审核，感谢您的支持，谢谢。");
	}
}
