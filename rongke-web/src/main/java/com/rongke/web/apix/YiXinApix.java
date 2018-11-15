package com.rongke.web.apix;

import com.alibaba.fastjson.JSONObject;
import com.rongke.utils.OrderUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.UUID;

import static jodd.JoddDefault.encoding;

/**
 * Created by Administrator on 2018/8/1 0001.
 */
public class YiXinApix {
    private static final Log log = LogFactory.getLog(YiXinApix.class);
    //    private static final String PARTNER_CODE = "qtEBjzc+a0AZOa1JpIBCaKv/AIho84B0";// 合作方标识编码过的
    public static final String PARTNER_CODE = "";// 合作方标识编码过的

    private static HttpURLConnection conn;

    /**
     * 芝麻分认证第一步:请求授权信息
     */
    public static String zhimaScoreFirst(JSONObject jsonObject) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String reqUrl = "https://www.mankoudai88.com/gateway/skynet/zm/score";
        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader("Content-type", "application/json;charset=UTF-8")
                .addParameter("idNo", jsonObject.getString("idNo"))//
                .addParameter("idName", jsonObject.getString("idName"))//
                .addParameter("phoneNo", jsonObject.getString("phoneNo"))//
                .addParameter("orderId", jsonObject.getString("orderId"))//
                .addParameter("redirect", jsonObject.getString("redirect"))//
                .addParameter("customUrl", jsonObject.getString("customUrl"))//
                .addParameter("channel", jsonObject.getString("channel"))//
                .addParameter("partner_code", PARTNER_CODE)//联系人
                .build();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = EntityUtils.toString(response.getEntity());
        log.info(result);
        return result;
    }

    /**
     * 芝麻分认证第二步:获取芝麻分数据
     */
    public static String zhimaScoreSecond(String orderId) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String reqUrl = "http://skynet.eloanx.com/zm/getZhimaScore";
        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader("Content-type", "application/json;charset=UTF-8")
                .addParameter("orderId", orderId)//
                .addParameter("partner_code", PARTNER_CODE)//联系人
                .build();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = EntityUtils.toString(response.getEntity());
        log.info(result);
        return result;
    }

    /**
     * 盈信查询状态
     */
    public static String phonetaskstatus(String idcard, String name, String phone) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String reqUrl = "http://skynet.eloanx.com/mx/taskstatus";
        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader("Content-type", "application/json;charset=UTF-8")
                .addParameter("idcard", idcard)//手机号
                .addParameter("name", name)//用户姓名
                .addParameter("phone", phone)//用户身份证号码
                .addParameter("partner_code", PARTNER_CODE)//联系人
                .build();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = EntityUtils.toString(response.getEntity());
        log.info(result);
        return result;
    }

    /**
     * 模拟请求
     *
     * @param
     * @param
     * @param
     * @return
     * @throws
     * @throws IOException
     */
    public static String preloan(JSONObject jsonObject) throws ParseException, IOException {
//        idNo--idName--mobileNo--idType
        String idNo = jsonObject.getString("idNo");
        String idName = jsonObject.getString("idName");
        String mobileNo = jsonObject.getString("mobileNo");
        String idType = jsonObject.getString("idType");
        String fileName = idNo + "-" + idName + "-" + mobileNo + "-" + idType;
        // 先从文件中查询
        String result = findPreLoanFile(fileName);
        if (result != null) {
            return result;
        }

        String body = "";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://www.mankoudai88.com/gateway/skynet/td/preloan");

        StringEntity sendentity = new StringEntity(jsonObject.toString(), "utf-8");//解决中文乱码问题
        sendentity.setContentEncoding("UTF-8");
        sendentity.setContentType("application/json");
        httpPost.setEntity(sendentity);

        System.out.println("请求地址：" + "http://skynet.eloanx.com/td/preloan");
        System.out.println("请求参数：" + sendentity.toString());

        httpPost.setHeader("partner_code", PARTNER_CODE);
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            body = EntityUtils.toString(entity, encoding);
            savePreLoanFile(fileName, body);
        }
        EntityUtils.consume(entity);
        response.close();
        return body;
    }

    /**
     * 将贷前审核信息存入文件
     *
     * @param fileName
     * @param content
     */
    public static void savePreLoanFile(String fileName, String content) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String path = request.getServletContext().getRealPath("/") + "/ATTACHMENT/preLoan/";
        String filePath = path + fileName + ".txt";

        File file_path = new File(path);
        if (!file_path.exists() && !file_path.isDirectory()) {
            file_path.mkdirs();
        }

        FileOutputStream fileOutputStream = null;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes("UTF-8"));
            fileOutputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从文件中查询贷前审核信息
     *
     * @param fileName
     * @return
     */
    public static String findPreLoanFile(String fileName) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String path = request.getServletContext().getRealPath("/") + "/ATTACHMENT/preLoan/";
        String filePath = path + fileName + ".txt";

        File file_path = new File(path);
        if (!file_path.exists() && !file_path.isDirectory()) {
            file_path.mkdirs();
        }

        if (new File(filePath).exists()) {
            StringBuilder content = new StringBuilder();
            try {
                InputStreamReader fReader = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
                BufferedReader br = new BufferedReader(fReader);
                String s = null;
                while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                    content.append(System.lineSeparator() + s);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content.toString();
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();
        String reqUrl = "https://www.mankoudai88.com/gateway/skynet/zm/score";
        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader("Content-type", "application/json;charset=UTF-8")
                .addParameter("idNo", "412724199803073712")//
                .addParameter("idName", "王云鹏")//
                .addParameter("phoneNo", "15168300680")//
                .addParameter("orderId", "20180827171300011e17977a5e1d4801")//
                .addParameter("redirect", "https://www.mankoudai88.com/gateway/skynet/zm/score")//
                .addParameter("customUrl", "www.baidu.com")//
                .addParameter("channel", "H5")//
                .addParameter("partner_code", PARTNER_CODE)//联系人
                .build();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);
    }
}