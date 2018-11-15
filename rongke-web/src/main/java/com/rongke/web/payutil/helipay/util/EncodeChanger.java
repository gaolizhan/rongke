package com.rongke.web.payutil.helipay.util;

import java.io.UnsupportedEncodingException;

public class EncodeChanger {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str = "0000||610422378899|李明昊天喆”、“璟|889.91|610422378859|李明叧叨叭叱叴叵叺叻叼叽|337.00|610422378822|李明|509.26";
		System.out.println(unicode2UnicodeEsc(str));

		System.out.println(chinaToUnicode(str));
		System.out.println(unicodeEsc2Unicode(unicode2UnicodeEsc(str)));

		System.out.println(unicodeEsc2Unicode(chinaToUnicode(str)));
	}

	public static String unicode2UnicodeEsc(String uniStr) {
		StringBuffer ret = new StringBuffer();
		if (uniStr == null) {
			return null;
		}
		int maxLoop = uniStr.length();
		for (int i = 0; i < maxLoop; ++i) {
			char character = uniStr.charAt(i);
			if (character <= '') {
				ret.append(character);
			} else {
				ret.append("\\u");
				String hexStr = Integer.toHexString(character);
				int zeroCount = 4 - hexStr.length();
				for (int j = 0; j < zeroCount; ++j) {
					ret.append('0');
				}
				ret.append(hexStr);
			}
		}
		return ret.toString();
	}

	public static String chinaToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int chr1 = (char) str.charAt(i);
			if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				result += "\\u" + Integer.toHexString(chr1);
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}

	public static String unicodeEsc2Unicode(String unicodeStr) {
		if (unicodeStr == null) {
			return null;
		}

		StringBuffer retBuf = new StringBuffer();
		int maxLoop = unicodeStr.length();
		for (int i = 0; i < maxLoop; ++i) {
			if (unicodeStr.charAt(i) == '\\') {
				if ((i < maxLoop - 5) && (((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U'))))
					try {
						retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
						i += 5;
					} catch (NumberFormatException e) {
						retBuf.append(unicodeStr.charAt(i));
					}
				else
					retBuf.append(unicodeStr.charAt(i));
			} else {
				retBuf.append(unicodeStr.charAt(i));
			}
		}

		return retBuf.toString();
	}
}
