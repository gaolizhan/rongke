package com.rongke.web.apix;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongke.utils.Md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class TongDunSDKAuth {
    private static String partner_code = "";
    private static String partner_key = "";

    public static JSONObject TongDunSDKQuery (String taskId){
        String url = "https://api.shujumohe.com/octopus/sdk.service.task.query/v3?partner_code="+partner_code+"&partner_key="+partner_key+"&task_id="+taskId;
        String body = "task_id="+taskId;
        String result = HttpClientUtil.sendPostSSLRequest(url,body,"UTF-8","application/x-www-form-urlencoded");
        return JSON.parseObject(result);
    }
    public static JSONObject TongDunXDBB (String body,String appName){
        String url = "https://api.tongdun.cn/bodyguard/apply/v4?partner_code=zhongchuangwl&partner_key=573f3b704be2407c9c935b741b11f3e0&app_name="+appName;
        String result = HttpClientUtil.sendPostSSLRequest(url,body,"UTF-8","application/x-www-form-urlencoded");
        return JSON.parseObject(result);
    }

    public static JSONObject youDunBank4(String parms) throws NoSuchAlgorithmException {
        Random random = new Random();
        String orderId = new Date().getTime()+""+random.nextInt();
        String sign = Md5.md5Encode(parms+"|"+"dd1eb421-ae6c-4345-b639-9c0a5047744a");
        String url = "https://api4.udcredit.com/dsp-front/4.1/dsp-front/default/pubkey/5456e4d9-9a4a-42f9-8c2b-e94383f7794b/product_code/O1001S0401/out_order_id/"+orderId+"/signature/"+sign;
        String result = HttpClientUtil.sendPostSSLRequest(url,parms,"UTF-8","application/json");
        return JSON.parseObject(result);
    }

    private static String md5(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data.toString().getBytes());
        return bytesToHex(md.digest());
    }

    private static String bytesToHex(byte[] ch) {
        StringBuffer ret = new StringBuffer("");
        for (int i = 0; i < ch.length; i++)
            ret.append(byteToHex(ch[i]));
        return ret.toString();
    }
    /**
     * 字节转换为16进制字符串
     */
    private static String byteToHex(byte ch) {
        String str[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F" };
        return str[ch >> 4 & 0xF] + str[ch & 0xF];
    }

    public static void main(String[] args) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("company_type", "私营");
        params.put("coborrower_home_address", "浙江省杭州市西湖区古荡新村2幢201");
        params.put("career", "半专业人员");
        params.put("occupation", "见习专员");
        params.put("contact3_relation", "test");
        params.put("customer_channel", "test");
        params.put("contact5_name", "test");
        params.put("work_phone", "0571-111111111");
        params.put("surety_name", "刘能");
        params.put("contact1_id_number", "test");
        params.put("contact5_id_number", "test");
        params.put("loan_purpose", "车贷");
        params.put("coborrower_id_number", "321282555555555555");
        params.put("coborrower_phone", "0571-10101010");
        params.put("surety_phone", "0571-222222222");
        params.put("house_property", "有房");
        params.put("contact2_id_number", "test");
        params.put("diploma", "研究生");
        params.put("annual_income", "100000-200000");
        params.put("id_number", "123123123123123000");
        params.put("surety_id_number", "321282333333333333");
        params.put("card_number", "6333380402564890000");
        params.put("contact1_mobile", "test");
        params.put("account_phone", "0571-42331233");
        params.put("loan_amount", "10000");
        params.put("qq_number", "313131313");
        params.put("monthly_income", "12000以上");
        params.put("apply_province", "四川");
        params.put("surety_mobile", "15223456789");
        params.put("contact4_relation", "test");
        params.put("contact5_mobile", "test");
        params.put("loan_term", "12");
        params.put("account_mobile", "13113131313");
        params.put("organization_address", "浙江省杭州市阿里巴巴西溪园区");
        params.put("contact3_mobile", "test");
        params.put("work_time", "1年以下");
        params.put("contact3_id_number", "test");
        params.put("contact3_name", "test");
        params.put("coborrower_name", "王五");
        params.put("loan_date", "2015-11-19");
        params.put("applyer_type", "在职");
        params.put("is_cross_loan", "否");
        params.put("industry", "金融业");
        params.put("surety_company_address", "浙江省杭州市下城区潮王路18号");
        params.put("contact2_name", "test");
        params.put("resp_detail_type", "test");
        params.put("apply_city", "成都");
        params.put("account_email", "212121212@qq.com");
        params.put("surety_home_address", "浙江省杭州市西湖区古荡新村");
        params.put("home_address", "浙江省杭州市西湖区古荡新村2幢101");
        params.put("marriage", "未婚");
        params.put("account_name", "张三");
        params.put("contact5_relation", "test");
        params.put("house_type", "商品房");
        params.put("contact_address", "浙江省杭州市西湖区古荡新村2幢101");
        params.put("contact1_name", "test");
        params.put("contact4_id_number", "test");
        params.put("contact2_relation", "test");
        params.put("coborrower_mobile", "17012345678");
        params.put("apply_channel", "app申请");
        params.put("contact4_name", "test");
        params.put("coborrower_company_address", "杭州市江干区2号大街928号");
        params.put("graduate_school", "南京大学");
        params.put("contact1_relation", "test");
        params.put("contact4_mobile", "test");
        params.put("organization", "阿里巴巴西溪园区");
        params.put("contact2_mobile", "test");
        JSONObject resultJO = TongDunSDKAuth.TongDunXDBB(com.rongke.service.HttpClientUtil.buildParams(params),"boniubao_and");
        System.out.print(resultJO);
    }
}
