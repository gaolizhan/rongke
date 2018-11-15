package com.rongke.wx.wxutils;


/**
 * <dl>
 * <dd>类/接口描述：配置文件工具
 * <dd>
 * <dl>
 *
 * @author 李巍
 * @2014-12-9 下午09:20:07
 */
public class ConfKit {

	/*private static Properties props = new Properties();

	static {
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("wechat.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String get(String key) {
		return props.getProperty(key);
	}

    public static void setProps(Properties p){
        props = p;
    }*/

    //测试
    /*private static String AppId="wx8b9d4cc8520e4597";
	private static String AppSecret="52c1d308fd15a8d1426ea0f1ed27801f";*/
    //正式
    private static String AppId = "wxc3d602154e900d0c";
    private static String AppSecret = "5917c32f6fa006e74e3845aa39c04eef";

    public static String getAppId() {
        return AppId;
    }

    public static void setAppId(String appId) {
        AppId = appId;
    }

    public static String getAppSecret() {
        return AppSecret;
    }

    public static void setAppSecret(String appSecret) {
        AppSecret = appSecret;
    }

    public static String get(String str) {
        if (str.equals("AppId")) {
            return getAppId();
        } else {
            return getAppSecret();
        }
    }
}
