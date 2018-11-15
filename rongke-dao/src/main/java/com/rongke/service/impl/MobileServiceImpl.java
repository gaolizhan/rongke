package com.rongke.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.LoanOrderMapper;
import com.rongke.mapper.MobileServiceMapper;
import com.rongke.mapper.ParamSettingMapper;
import com.rongke.model.*;
import com.rongke.service.HttpClientUtil;
import com.rongke.service.MobileService;
import com.rongke.service.TongdunAuditService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

@Service
public class MobileServiceImpl extends ServiceImpl<MobileServiceMapper,TongdunLog > implements MobileService {

    private static final long   WAIT_TIME    = 5 * 1000;

    //永讯配置
    private static final String yongxunUrl = "https://api.yongxunzhengxin.com/risk/v1/overdue";
    private static final String yongxunAPIKey =  "xxxxx";
    private static final String yongxunAPISecret = "xxxxxxx";
    //同盾配置
    private static final String tongdunSubmitUrl = "https://apitest.tongdun.cn/preloan/apply/v5";
    private static final String tongdunQueryUrl = "https://apitest.tongdun.cn/preloan/report/v9";
    private static final String tongdunPartnerCode = "xxxx";
    private static final String tongdunPartnerKey = "xxxxx";
    private static final String tongdunAppName = "xxx";

    private HttpsURLConnection conn;
    private SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();

    private static HttpClient client = null;

    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(128);
        cm.setDefaultMaxPerRoute(128);
        client = HttpClients.custom().setConnectionManager(cm).build();
    }



    @Override
    public Map<String,String> yongxunRun(String mobile, String cycle) {
        Map<String,String> resultMap = new HashMap<>();
        try{
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("mobile",mobile);
            paramsMap.put("cycle",cycle);
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("authorization-key",yongxunAPIKey);
            headerMap.put("authorization-timestamp", String.valueOf(new Date().getTime()));
            headerMap.put("authorization-sign", DigestUtils.shaHex(yongxunAPIKey+String.valueOf(new Date().getTime())+yongxunAPISecret));

            String a = HttpClientUtil.sendPost(yongxunUrl,paramsMap,headerMap);
            System.out.print(a);
            JSONObject rmJObject = JSON.parseObject(a);
            resultMap.put("code",rmJObject.getString("code"));
            resultMap.put("msg",rmJObject.getString("msg"));
            JSONObject data = rmJObject.getJSONObject("data");
            resultMap.put("mobile",data.getString("mobile"));
            resultMap.put("count",data.getString("count"));
            resultMap.put("amt",data.getString("amt"));
            resultMap.put("province",data.getString("province"));
            resultMap.put("city",data.getString("city"));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return resultMap;
        }
    }

    @Override
    public TongdunLog TongDunRun(String userId,LoanOrder lo,Map<String, String> paramsMap) {
        System.out.print("+++++++++++++"+paramsMap.toString());
        TongdunLog tongdunLog = new TongdunLog();
        try{
            tongdunLog.setUserId(Long.valueOf(userId));
            LoanOrder ob = new LoanOrder();
            ob.setUserId(Long.valueOf(userId));
            tongdunLog.setOrderId(lo.getId());
            String tongdunUrl = tongdunSubmitUrl + "?partner_code=" + tongdunPartnerCode + "&partner_key=" + tongdunPartnerKey + "&app_name=" + tongdunAppName;
//            String resultMsg = postForm(tongdunUrl, paramsMap, headerMap, 5000,5000);
            String body = HttpClientUtil.buildParams(paramsMap);
            String resultMsg = HttpClientUtil.sendPostSSLRequest(tongdunUrl,body,"UTF-8","application/x-www-form-urlencoded");
            System.out.print("++++++++++++"+resultMsg);
            JSONObject resultJO = JSON.parseObject(resultMsg);
            tongdunLog.setSuccess(resultJO.getString("success"));
            tongdunLog.setReasonCode(resultJO.getString("reason_code"));
            tongdunLog.setReasonDesc(resultJO.getString("reason_desc"));
            tongdunLog.setReportId(resultJO.getString("report_id"));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return tongdunLog;
        }
    }

    @Override
    public JSONObject tongDunGet(String reportId) {
        String result = HttpClientUtil.sendGetSSLRequest("https://apitest.tongdun.cn/preloan/report/v9?partner_code="+tongdunPartnerCode+"&partner_key="+tongdunPartnerKey+"&app_name="+tongdunAppName+"&report_id="+reportId,"UTF-8");
        System.out.print("+-+-+-+-+-+-+-+-+-+-+-+"+result);
        return JSON.parseObject(result);
    }

    @Autowired
    private MobileServiceMapper mobileServiceMapper;
    @Override
    public List<TongdunLog> selectListForJob() {
        return mobileServiceMapper.selectListForJob();
    }

    /**
     * 同盾贷后监控
     */

    @Autowired
    private TongdunAuditService tongdunAuditService;
    @Override
    public JSONObject tongDunMonitor(LoanOrder loanOrder) {
        EntityWrapper ew = new EntityWrapper();
        ew.eq("user_id",loanOrder.getUserId());
        TongdunAudit tongdunAudit = tongdunAuditService.selectOne(ew);
        String body = "report_id="+tongdunAudit.getReportId()+"&loan_term="+loanOrder.getLimitDays()+"&loan_term_unit=DAY&loan_date="+loanOrder.getGiveTime().getTime()
                + "&loan_amount="+ loanOrder.getNeedPayMoney()+"&begin_scan_time="+(loanOrder.getGiveTime().getTime()+86400000L)+"&end_scan_time="+(loanOrder.getGiveTime().getTime()+2628000000L);
        String result = HttpClientUtil.sendPostSSLRequest("https://apitest.tongdun.cn/postloan/monitor/v3"+"?partner_code=" + tongdunPartnerCode + "&partner_key=" + tongdunPartnerKey
                ,body,"UTF-8","application/x-www-form-urlencoded");
        return JSON.parseObject(result);
    }

    /**
     * 提交form表单
     *
     * @param url
     * @param params
     * @param connTimeout
     * @param readTimeout
     * @return
     * @throws ConnectTimeoutException
     * @throws SocketTimeoutException
     * @throws Exception
     */
    public static String postForm(String url, Map<String, String> params, Map<String, String> headers, Integer connTimeout,Integer readTimeout) throws ConnectTimeoutException,
            SocketTimeoutException, Exception {

        HttpClient client = null;
        HttpPost post = new HttpPost(url);
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                Set<Map.Entry<String, String>> entrySet = params.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                post.setEntity(entity);
            }

            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置参数
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());
            HttpResponse res = null;
            if (url.startsWith("https")) {
                // 执行 Https 请求.
                client = createSSLInsecureClient();
                res = client.execute(post);
            } else {
                // 执行 Http 请求.
                client = MobileServiceImpl.client;
                res = client.execute(post);
            }
            return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
        } finally {
            post.releaseConnection();
            if (url.startsWith("https") && client != null
                    && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
    }

    /**
     * 创建 SSL连接
     * @return
     * @throws GeneralSecurityException
     */
    private static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl)
                        throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert)
                        throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns,
                                   String[] subjectAlts) throws SSLException {
                }

            });

            return HttpClients.custom().setSSLSocketFactory(sslsf).build();

        } catch (GeneralSecurityException e) {
            throw e;
        }
    }


}
