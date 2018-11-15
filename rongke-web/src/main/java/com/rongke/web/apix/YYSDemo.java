package com.rongke.web.apix;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Scanner;

/**
 * 运营商接入流程示例
 * Created by chris on 2016/11/14.
 */
public class YYSDemo {
    private final static String APIX_KEY = "apix-key";

    private static String apixKey = "";

    /**
     * 获取图片验证码
     *
     * @param phoneNo 手机号码
     * @return 响应报文d
     * @throws IOException
     */
    public static String getCapcha(String phoneNo) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        //获取验证码接口地址
        String reqUrl = "http://e.apix.cn/apixanalysis/mobile/yys/phone/capcha";

        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader(APIX_KEY, apixKey)
                .addParameter("phone_no", phoneNo)//手机号
//                .addParameter("name" , "")//用户姓名
//                .addParameter("cert_no", "")//用户身份证号码
//                .addParameter("contact_list" , "")//联系人
                .build();

        CloseableHttpResponse response = client.execute(request);

        return EntityUtils.toString(response.getEntity());
    }

    /**
     * 登录
     *
     * @param phoneNo  手机号码
     * @param passwd   密码
     * @param capcha   图片验证码
     * @param callback 回调地址
     * @return 响应报文
     * @throws IOException
     */
    public static String login(String phoneNo, String passwd, String capcha, String callback) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        //获取验证码接口地址
        String reqUrl = "http://e.apix.cn/apixanalysis/mobile/yys/phone/login";

        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader(APIX_KEY, apixKey)
                .addParameter("phone_no", phoneNo)//手机号
                .addParameter("passwd", passwd)//服务密码
                .addParameter("capcha", capcha)//验证码
                .addParameter("callback", callback)//回调地址
                .build();

        CloseableHttpResponse response = client.execute(request);

        return EntityUtils.toString(response.getEntity());
    }

    /**
     * 获取短信验证码
     *
     * @param phoneNo 手机号码
     * @return 响应报文
     * @throws IOException
     */
    public static String getSmsCode(String phoneNo) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        //获取验证码接口地址
        String reqUrl = "http://e.apix.cn/apixanalysis/mobile/yys/phone/smsCode/getSms";

        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader(APIX_KEY, apixKey)
                .addParameter("phone_no", phoneNo)//手机号
                .build();

        CloseableHttpResponse response = client.execute(request);

        return EntityUtils.toString(response.getEntity());
    }



    /**
     * 短信验证码校验
     *
     * @param phoneNo 手机号码
     * @param smsCode 短信验证码
     * @param name    姓名（陕西，甘肃电信需要）
     * @param certNo  身份证号码（陕西，甘肃电信需要）
     * @param capcha  图片验证码（江西移动需要）
     * @return
     * @throws IOException
     */
    public static String verifySmsCode(String phoneNo, String smsCode, String name, String certNo, String capcha) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        //获取验证码接口地址
        String reqUrl = "http://e.apix.cn/apixanalysis/mobile/yys/phone/smsCode/verify";

        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader(APIX_KEY, apixKey)
                .addParameter("phone_no", phoneNo)//手机号
                .addParameter("sms_code", smsCode)//短信验证码
                .addParameter("name", name)//用户姓名，陕西，甘肃电信必须提交此参数
                .addParameter("cert_no", certNo)//身份证号码，陕西，甘肃电信必须提交此参数
                .addParameter("capcha", capcha)//图片验证码 江西移动需要此参数
                .build();

        CloseableHttpResponse response = client.execute(request);

        return EntityUtils.toString(response.getEntity());
    }

    /**
     * 获取数据
     *
     * @param url   接口地址
     * @param token 查询码
     * @return
     * @throws IOException
     */
    public static JSONObject getData(String url, String token) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(url)
                .addHeader(APIX_KEY, apixKey)
                .addParameter("query_code", token)//图片验证码 江西移动需要此参数
                .build();

        CloseableHttpResponse response = client.execute(request);

        //将字符串转换成jsonObject对象
        JSONObject myJsonObject = new JSONObject();
        myJsonObject = myJsonObject.parseObject(EntityUtils.toString(response.getEntity()));
        return myJsonObject;

    }


    public static void main(String[] args) throws IOException, InterruptedException {
        //初始必填参数
        String phoneNo = "18157161106";//手机号码
        String passwd = "916937";//密码

        //非必填参数
        String callback = "";//回掉地址

        //程序声明参数
        String name = "";
        String certNo = "";
        String capcha = "";
        String smsCode = "";
        String resp = getCapcha(phoneNo);

        JSONObject json = new JSONObject();
        json = json.parseObject(resp);
        String retCode = json.getString("errorCode");

        if (StringUtils.equals(retCode, "0")) {
            String codeType = json.getString("imgUrl");
            if (StringUtils.isBlank(codeType)) {
                System.out.println("登录不需要验证码");
            } else if (StringUtils.equals("smsCode", codeType)) {
                System.out.println("请输入收到的短信验证码：");
                capcha = new Scanner(System.in).next();
            } else {
                String imgUrl = json.getString("imgUrl");
                System.out.println(String.format("请识别返回的图片验证码，验证码地址为[%s]：", imgUrl));
                capcha = new Scanner(System.in).next();
            }
        } else {
            System.out.println(String.format("请求失败！ | [%s]", json.getString("errorMsg")));
        }

        //第一步 获取登录验证码


        //第二步 登录
        resp = login(phoneNo, passwd, capcha, callback);

        json = new JSONObject();
        json = json.parseObject(resp);
        retCode = json.getString("errorCode");
        if (StringUtils.equals(retCode, "0")) {
            Boolean isFinish = json.getBoolean("result");
            if (isFinish) {
                String token = json.getString("token");
                System.out.println(String.format("恭喜，授权完成！ | 授权token为[%s]", token));
                System.exit(0);
            } else {
                System.out.println("授权还未结束，需要执行第三四步，稍等！");
            }
        } else {
            System.out.println(String.format("请求失败！ | [%s]", json.getString("errorMsg")));
        }

        String operator = json.getString("operator");

        if (StringUtils.contains(operator, "移动")) {
            //需要等待60秒，才可获取短信验证码
            Thread.sleep(50000);
        }

        //第三步 获取短信验证码
        resp = getSmsCode(phoneNo);

        json = new JSONObject();
        json = json.parseObject(resp);
        retCode = json.getString("errorCode");

        if (StringUtils.equals(retCode, "0")) {
            if (StringUtils.contains(operator, "电信")) {
                if (StringUtils.contains(operator, "陕西") || StringUtils.contains(operator, "甘肃")) {
                    System.out.println("请输入用户姓名:");
                    name = new Scanner(System.in).next();
                    System.out.println("请输入用户身份证号码:");
                    certNo = new Scanner(System.in).next();

                }
            } else if (StringUtils.contains(operator, "移动")) {
                if (StringUtils.contains(operator, "江西")) {
                    String imgUrl = json.getString("imgUrl");
                    System.out.println(String.format("请识别返回的图片验证码，验证码地址为[%s]：", imgUrl));
                    capcha = new Scanner(System.in).next();
                }
            }

            System.out.println("请输入获取到的短信验证码:");
            smsCode = new Scanner(System.in).next();
        } else {
            System.out.println(String.format("请求失败！ | [%s]", json.getString("errorMsg")));
        }

        //第四步 短信验证码校验
        resp = verifySmsCode(phoneNo, smsCode, name, certNo, capcha);
        json = new JSONObject();
        json = json.parseObject(resp);
        retCode = json.getString("errorCode");

        if (StringUtils.equals(retCode, "0")) {
            String token = json.getString("token");
            System.out.println(String.format("恭喜，授权完成！ | 授权token为[%s]", token));
            System.exit(0);
        } else {
            System.out.println(String.format("请求失败！ | [%s]", json.getString("errorMsg")));
        }

/*       //第五步 获取报告
        String token = "e46a2686-7726-11e7-9a23-00163e03f3ba";//获取到的token
        //String reqUrl ="http://e.apix.cn/apixanalysis/mobile/retrieve/phone/data/analyzed"; //获取分析报告url
        String reqUrl = "http://e.apix.cn/apixanalysis/mobile/mobile/life/phone";//获取原始报告url
        String retData = getData(reqUrl, token);//获取分析报告
        System.out.println(retData);*/
    }


}
