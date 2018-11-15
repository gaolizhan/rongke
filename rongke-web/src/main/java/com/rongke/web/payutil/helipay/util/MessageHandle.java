package com.rongke.web.payutil.helipay.util;

import com.rongke.web.payutil.helipay.annotation.FieldEncrypt;
import com.rongke.web.payutil.helipay.annotation.SignExclude;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by heli50 on 2018-06-25.
 */
public class MessageHandle {

	private static final Log log = LogFactory.getLog(MessageHandle.class);


	private static final String CERT_PATH = "key/helipay.cer";    //合利宝cert
	private static final String PFX_PATH = "key/helipay.pfx";        //商户pfx
	private static final String KEY_PATH = "key/pri-helipay.key";
	private static final String PFX_PWD = "Fintech";    //pfx密码
	private static final String ENCRYPTION_KEY = "encryptionKey";
	private static final String SPLIT = "&";
	private static final String SIGN = "sign";

	static {

	}

	/**
	 * 获取map
	 */
	public static Map getReqestMap(Object bean) throws Exception {

		Map retMap = new HashMap();

		boolean isEncrypt = false;
		String aesKey = AES.generateString(16);
		StringBuilder sb = new StringBuilder();

		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String key = field.toString().substring(field.toString().lastIndexOf(".") + 1);
			String value = (String) field.get(bean);
			if (value == null) {
				value = "";
			}
			//查看是否有需要加密字段的注解,有则加密
			//这部分是将需要加密的字段先进行加密
			if (field.isAnnotationPresent(FieldEncrypt.class) && StringUtils.isNotEmpty(value)) {
				isEncrypt = true;
				value = AES.encryptToBase64(value, aesKey);
			}

			//字段没有@SignExclude注解的拼签名串
			//这部分是把需要参与签名的字段拼成一个待签名的字符串
			if (!field.isAnnotationPresent(SignExclude.class)) {
				sb.append(SPLIT);
				sb.append(value);
			}

			retMap.put(key, value);
		}

		//如果有加密的，需要用合利宝的公钥将AES加密的KEY进行加密使用BASE64编码上送
		if (isEncrypt) {
			String path = MessageHandle.class.getResource("/").getPath()+CERT_PATH;
			PublicKey publicKey = RSA.getPublicKeyByCert(path);
			String encrytionKey = RSA.encodeToBase64(aesKey, publicKey, ConfigureEncryptAndDecrypt.KEY_ALGORITHM);
			retMap.put(ENCRYPTION_KEY, encrytionKey);
		}


		log.info("原签名串：" + sb.toString());
        String path = MessageHandle.class.getResource("/").getPath()+PFX_PATH;
        PrivateKey privateKey = RSA.getPrivateKey(path, PFX_PWD);
        String sign = RSA.sign(sb.toString(), privateKey);
		retMap.put(SIGN, sign);
		log.info("签名sign：" + sign);


		return retMap;
	}

	public static String sign(String sb) {
		//使用商户的私钥进行签名
		try {
			String path  = MessageHandle.class.getResource("/").getPath()+KEY_PATH;
			PrivateKey privateKey = RSA.getPrivateKey2(path, PFX_PWD);
			Signature signature = Signature.getInstance("MD5withRSA");
			byte[] dataByte = sb.getBytes("UTF-8");
			signature.initSign(privateKey);

				signature.update(dataByte);

			return new String(Base64.encode(signature.sign()));
		} catch (Exception e) {
			e.printStackTrace();
		}
//        String sign = RSA.sign(EncodeChanger.unicodeEsc2Unicode(sb), privateKey);
        return null;
	}

	public static boolean checkSign(Object bean) throws Exception {

		boolean flag = false;

		StringBuilder sb = new StringBuilder();

		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		String sign = "";
		for (Field field : fields) {
			field.setAccessible(true);
			String key = field.toString().substring(field.toString().lastIndexOf(".") + 1);
			String value = (String) field.get(bean);
			if (value == null) {
				value = "";
			}

			if (SIGN.equals(key)) {
				sign = value;
			}

			//字段没有@SignExclude注解的拼签名串
			//这部分是把需要参与签名的字段拼成一个待签名的字符串
			if (!field.isAnnotationPresent(SignExclude.class)) {
				sb.append(SPLIT);
				sb.append(value);
			}

		}
		log.info("response验签原签名串：" + sb.toString());

		//使用合利宝的公钥进行验签
		String path = MessageHandle.class.getResource("/").getPath()+CERT_PATH;
		PublicKey publicKey = RSA.getPublicKeyByCert(path);
		flag = RSA.verifySign(sb.toString(), sign, publicKey);
		if (flag) {
			log.info("验签成功");
		} else {
			log.info("验签失败");
		}
		return flag;

	}

	public static String getMACAddress() {
		InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();

		// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

		// 下面代码是把mac地址拼装成String
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}

		// 把字符串所有小写字母改为大写成为正规的mac地址并返回
		return sb.toString().toUpperCase().replaceAll("-", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  "11111111111111";
	}



}
