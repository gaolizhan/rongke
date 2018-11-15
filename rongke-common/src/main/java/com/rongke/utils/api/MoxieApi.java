package com.rongke.utils.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by Administrator on 2018/8/1 0001.
 */
public class MoxieApi {
    private static final Log log = LogFactory.getLog(MoxieApi.class);
    // 沙箱环境
//    public static final String API_KEY = "a582e6b672cd4dfa87df28cab5cb514c";
//    public static final String TOKEN = "73686acfa5b640ad9d35fabf8c7e9711";

    // 生产环境
    public static final String API_KEY =    "b1c05927f1654f9f81deeec5e51c613e";
    public static final String TOKEN =     "638edf72b82140b8ad2aec4c83f54103";

    public static final String BACK_URL =   "http://app.yironghua.cn/open/zhimaSuccess.html";

    private static HttpURLConnection conn;

    /**
     * 获取魔蝎原始数据
     *
     * @param mobile
     * @param taskId
     * @return
     * @throws IOException
     */
    public static String getMxData(String mobile, String taskId,String realPath) throws IOException {
        log.info("<-----------getMxData----------> mxData");
        String path = realPath + "/ATTACHMENT/mx/data";
        String fileName = taskId + ".txt";
        String result = findMxDataFile(path, fileName);
        if (result != null && result.length()>1000) {
            return result;
        }
        log.info("<-----------getMxData----------> start");

        CloseableHttpClient client = HttpClients.createDefault();
        String reqUrl = "https://api.51datakey.com/carrier/v3/mobiles/" + mobile + "/mxdata-ex";
        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader("Authorization", "token " + TOKEN)
                .addParameter("task_id", taskId)
                .build();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity);
            saveMxDataFile(path, fileName, result);
        }
//        log.info(result);
        return result;
    }


    /**
     * 获取魔蝎报告数据
     *
     * @param mobile
     * @param name
     * @param idcard
     * @param taskId
     * @return
     * @throws IOException
     */
    public static String getMxReportData(String mobile, String name, String idcard, String taskId,String realPath) throws IOException {
        log.info("<-----------getMxReportData----------> mxDataReport");
        String path = realPath + "/ATTACHMENT/mx/reportData";
        String fileName = taskId + ".txt";
        String result = findMxDataFile(path, fileName);
        if (result != null && result.length()>1000) {
            return result;
        }
        log.info("<-----------getMxReportData----------> start");
        CloseableHttpClient client = HttpClients.createDefault();
        String reqUrl = "https://api.51datakey.com/carrier/v3/mobiles/" + mobile + "/mxreport";
        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader("Authorization", "token " + TOKEN)
                .addParameter("name", name)
                .addParameter("idcard", idcard)
                .addParameter("task_id", taskId)
                .build();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity);
            saveMxDataFile(path, fileName, result);
        }
//        log.info(result);
        return result;
    }

    /**
     * 报告数据存入文件
     *
     * @param fileName
     * @param content
     */
    public static void saveMxDataFile(String path, String fileName, String content) {
        String filePath = path + "/" + fileName;

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
     * 从文件中查询数据
     *
     * @param fileName
     * @return
     */
    public static String findMxDataFile(String path, String fileName) {
        String filePath = path + "/" + fileName;

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

    /**
     * 获取魔蝎报告状态
     *
     * @param mobile
     * @param taskId
     * @return
     * @throws IOException
     */
    public static String getMxReportStatus(String mobile, String taskId) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String reqUrl = "https://api.51datakey.com/report/status/v1/carrier/status";
        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader("Authorization", "apikey " + API_KEY)
                .addParameter("userName", mobile)
                .addParameter("taskId", taskId)
//                .addParameter("userId", userId)
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
     * 获取魔蝎报告 html
     *
     * @param message
     * @return
     * @throws IOException
     */
    public static String getMxReportHtml(String message) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String reqUrl = "https://tenant.51datakey.com/carrier/report_data?data=" + message;
        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
//                .addHeader("Authorization", "apikey " + API_KEY)
//                .addParameter("data", message)
//                .addParameter("userId", userId)
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

    public static void main(String[] args) throws IOException {
        String result = null;
        CloseableHttpClient client = HttpClients.createDefault();
        String reqUrl = "https://api.51datakey.com/carrier/v3/mobiles/" + "13429679833" + "/mxdata-ex";
        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(reqUrl)
                .addHeader("Authorization", "token " + TOKEN)
                .addParameter("task_id", "f3eff530-e805-11e8-976c-00163e10becc")
                .build();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity);
            System.out.println(result);
//            saveMxDataFile(path, fileName, result);
        }
    }

//    public static void main(String[] args) throws IOException {
//        String result_data = getMxData("15067888320", "3f16e550-b0e3-11e8-8dea-00163e0d2629","");
//        String result_reportData = getMxReportData("18857627857", "黄圣豪", "331021199308310014", "3f16e550-b0e3-11e8-8dea-00163e0d2629",
//                "/");
//        String result_status = getMxReportStatus("18857627857", "3f16e550-b0e3-11e8-8dea-00163e0d2629");
//        String result_report = getMxReportHtml("aX92rOy5T05m3hyBD%2FhHsn72RGOmhhlK2lJ2SblzcBzolkCnoPv0979ydKgkq4N7LzP7EHevnRDrqfEVtsg6cv8oUQ0fULW5DbfqJzjPeErfGGe1SXF1Qh4KQIg7Kl8iWIDssRnRXqZzl8GUfC3Tk22HPCmb%2Bt5E%2FvyYIc8z5zueJLsRvv0u4vrDbDATgOfe");

//        System.out.println(result_data);
//    }
}