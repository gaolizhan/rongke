package com.rongke.web.apix;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

/**
 * 
 * 封装了采用HttpClient发送HTTP请求的方法
 * 
 * @see 本工具所采用的是HttpComponents-Client-4.3.2
 * @see ========================================================================
 *      == =========================
 * @see sendPostSSLRequest 发送https post请求，  兼容 http post 请求    
 * @see sendGetSSLRequest  发送https get请求，  兼容 http get 请求  
 * @create 2016年10月12日18:52:06
 * @author lilin
 */

public class HttpClientUtil {
	
	/**
	 * 连接超时
	 */
	public static final int connTimeout=10000;
    /**
     * 响应超时
     */
    public static final int readTimeout=10000;
    /**
     * 默认字符编码
     */
    public static final String charset="UTF-8";
    private static HttpClient client = null;
    
    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(128);
        cm.setDefaultMaxPerRoute(128);
        client = HttpClients.custom().setConnectionManager(cm).build();
    }
    
    public static String postParameters(String url, String parameterStr){
        return sendPostSSLRequest(url,parameterStr,charset,"application/json");
    }
    
    public static String postParameters(String url, String parameterStr,String charset){
        return sendPostSSLRequest(url,parameterStr,charset,"application/json");
    }
    
    public static String postParameters(String url, Map<String, String> params) throws ConnectTimeoutException,  
     SocketTimeoutException, Exception {
         return postForm(url, params, null, connTimeout, readTimeout);
     }
    
    public static String postParameters(String url, Map<String, String> params, Integer connTimeout,Integer readTimeout) throws ConnectTimeoutException,  
    SocketTimeoutException, Exception {
         return postForm(url, params, null, connTimeout, readTimeout);
     }
      
    public static String get(String url) {  
        return sendGetSSLRequest(url, charset);  
    }
    
    public static String get(String url, String charset) {  
        return sendGetSSLRequest(url, charset);  
    } 

    /** 
     * 发送一个 Post 请求, 使用指定的字符集编码. 
     *  
     * @param url 
     * @param body RequestBody 
     * @param mimeType 例如 application/xml "application/x-www-form-urlencoded" a=1&b=2&c=3
     * @param charset 编码 
     * @param connTimeout 建立链接超时时间,毫秒. 
     * @param readTimeout 响应超时时间,毫秒. 
     * @return ResponseBody, 使用指定的字符集编码. 
     * @throws ConnectTimeoutException 建立链接超时异常 
     * @throws SocketTimeoutException  响应超时 
     * @throws Exception 
     */  
    public static String sendPostSSLRequest(String url, String body ,String charset,String mimeType){
        HttpClient client = null;
        HttpPost post = new HttpPost(url);
        String result = "通信失败";
        try {
            if (StringUtils.isNotBlank(body)) {
                HttpEntity entity = new StringEntity(body, ContentType.create(mimeType, charset));
                post.setEntity(entity);
            }
            // 设置参数
            Builder customReqConf = RequestConfig.custom();
            customReqConf.setConnectTimeout(connTimeout);
            customReqConf.setSocketTimeout(readTimeout);
            post.setConfig(customReqConf.build());

            HttpResponse res;
            if (url.startsWith("https")) {
                // 执行 Https 请求.
				client = createSSLInsecureClient();
				
				res = client.execute(post);
				
            } else {
                // 执行 Http 请求.
                client = HttpClientUtil.client;
                res = client.execute(post);
            }
            result = IOUtils.toString(res.getEntity().getContent(), charset);
        } catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
            post.releaseConnection();
            if (url.startsWith("https") && client != null&& client instanceof CloseableHttpClient) {
                try {
					((CloseableHttpClient) client).close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
        return result;
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
                Set<Entry<String, String>> entrySet = params.entrySet();  
                for (Entry<String, String> entry : entrySet) {  
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
                }  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);  
                post.setEntity(entity);  
            }
            
            if (headers != null && !headers.isEmpty()) {  
                for (Entry<String, String> entry : headers.entrySet()) {  
                    post.addHeader(entry.getKey(), entry.getValue());  
                }  
            }  
            // 设置参数  
            Builder customReqConf = RequestConfig.custom();  
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
                client = HttpClientUtil.client;  
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
     * 发送一个 GET 请求 
     *  
     * @param url 
     * @param charset 
     * @param connTimeout  建立链接超时时间,毫秒. 
     * @param readTimeout  响应超时时间,毫秒. 
     * @return 
     * @throws ConnectTimeoutException   建立链接超时 
     * @throws SocketTimeoutException   响应超时 
     * @throws Exception 
     */  
    public static String sendGetSSLRequest(String url, String charset){ 
        
        HttpClient client = null;  
        HttpGet get = new HttpGet(url);  
        String result = "通信失败";  
        try {  
            // 设置参数  
            Builder customReqConf = RequestConfig.custom();  
            customReqConf.setConnectTimeout(connTimeout);  
            customReqConf.setSocketTimeout(readTimeout);  
            get.setConfig(customReqConf.build());  
  
            HttpResponse res = null;  
  
            if (url.startsWith("https")) {  
                // 执行 Https 请求.  
				client = createSSLInsecureClient();
				res = client.execute(get);
				
            } else {  
                // 执行 Http 请求.  
                client = HttpClientUtil.client;  
                res = client.execute(get);  
            }  
  
            result = IOUtils.toString(res.getEntity().getContent(), charset);  
        } catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}     finally {  
            get.releaseConnection();  
            if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {  
                try {
					((CloseableHttpClient) client).close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
            }  
        }  
        return result;  
    }  
    
    
    /** 
     * 从 response 里获取 charset 
     *  
     * @param ressponse 
     * @return 
     */  
    @SuppressWarnings("unused")  
    private static String getCharsetFromResponse(HttpResponse ressponse) {  
        // Content-Type:text/html; charset=GBK  
        if (ressponse.getEntity() != null  && ressponse.getEntity().getContentType() != null && ressponse.getEntity().getContentType().getValue() != null) {  
            String contentType = ressponse.getEntity().getContentType().getValue();  
            if (contentType.contains("charset=")) {  
                return contentType.substring(contentType.indexOf("charset=") + 8);  
            }  
        }  
        return null;  
    }  
    
    
    
    /**
     * 创建 SSL连接
     * @return
     * @throws GeneralSecurityException
     */
    private static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                        public boolean isTrusted(X509Certificate[] chain,String authType) throws CertificateException {
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
    
    public static void main(String[] args) {  
    	StopWatch w = new StopWatch();
    	w.start();
        String str= sendGetSSLRequest("https://kyfw.12306.cn/otn/leftTicket/init","UTF-8");
        /*Map<String,String> map = new HashMap<String,String>();
        map.put("name", "111");
        map.put("page", "222");
        String str= postForm("https://localhost:443/ssl/test.shtml",map,null, 10000, 10000);*/
        System.out.println(str);
        w.stop();
        System.err.println(w.getTime());
    }
}
