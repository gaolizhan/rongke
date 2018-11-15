package com.rongke.web.config;

import com.rongke.enums.FileType;
import com.rongke.enums.FileType1;
import com.rongke.utils.RequestUtils;
import com.rongke.utils.URLUtils;
import com.rongke.web.upload.FileSeeController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 项目配置
 */
@Controller
public class Config {
    /**
     * 控制层类名结尾
     */
    private String controllerFix = "Controller";
    /**
     * 工具模块基本报名
     */
    private static final String commonBasePackage = "com.rongke.web";
    /**
     * 工具模块业务报名
     */
    public static final String commonBottomPackage = commonBasePackage + ".upload";

    /**
     * 页面访问地址
     */
    private String pageDomain;

    public String getPageDomain() {
        return pageDomain;
    }

    public void setPageDomain(String pageDomain) {
        this.pageDomain = pageDomain;
    }

    /**
     * URL分隔符
     */
    private String urlSplit = "|||";

    /**
     * 密钥对
     */
    private Map<String, String> keyPair;

    public Map<String, String> getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(Map<String, String> keyPair) {
        this.keyPair = keyPair;
    }

    public String getUrlSplit() {
        return urlSplit;
    }

    public void setUrlSplit(String urlSplit) {
        this.urlSplit = urlSplit;
    }

    public String getControllerFix() {
        return controllerFix;
    }

    public void setControllerFix(String controllerFix) {
        this.controllerFix = controllerFix;
    }

    private String fileUploadUri;

    private String getFileUploadUri() {
        return fileUploadUri;
    }

    public void setFileUploadUri(String fileUploadUri) {
        this.fileUploadUri = fileUploadUri;
    }

    /**
     * 项目名字
     */
    private String appName = Config.class.getClassLoader().getResource(".").getFile();
    /**
     * 顶级域名
     */
    private String projectDomain;

    public String getProjectDomain() {
        return projectDomain;
    }

    public void setProjectDomain(String projectDomain) {
        this.projectDomain = projectDomain;
    }

    /**
     * 项目名字中文
     */
    private String appNameCH;

    public String getAppNameCH() {
        return appNameCH;
    }

    public Integer getPageSizeDefault() {
        return pageSizeDefault;
    }

    public void setPageSizeDefault(Integer pageSizeDefault) {
        this.pageSizeDefault = pageSizeDefault;
    }

    public void setAppNameCH(String appNameCH) {
        this.appNameCH = appNameCH;
    }

    private Integer pageSizeDefault;
    /**
     * 登陆图片验证码存入会话key
     */
    private String loginPictureSessionKey;
    /**
     * 控制层扫描包路径
     */
    private String controllerPackagePath;

    public String getControllerPackagePath() {
        return controllerPackagePath;
    }

    public void setControllerPackagePath(String controllerPackagePath) {
        this.controllerPackagePath = controllerPackagePath;
    }

    /**
     * 登陆后存入cookie的令牌名字
     */
    private String loginTokenCookieName;
    /**
     * 登陆后存入cookie的令牌过期时间
     */
    private Integer loginTokenCookieExpirationTime;
    /**
     * 文件上穿路径
     */
    private String fileUploadPath;
    /**
     * 资源文件路径
     */
    private String resourcesPath = "/resources";
    /**
     * 资源文件远程地址
     */
    private String resourcePathRemote;
    /**
     * 跨域域名
     */
    private String CORSDomain = "*";

    public String getCORSDomain() {
        return CORSDomain;
    }

    public void setCORSDomain(String CORSDomain) {
        this.CORSDomain = CORSDomain;
    }

    /**
     * 接口地址
     */
    private String apiHost;

    public String getApiHost() {
        return apiHost;
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    public String getResourcePathRemote() {
        return resourcePathRemote;
    }

    public void setResourcePathRemote(String resourcePathRemote) {
        this.resourcePathRemote = resourcePathRemote;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    /**
     * 邮件发送账号
     */
    private String fromEmail;
    /**
     * 最低购买金额
     */
    private Double buyAmountLowest;

    public Double getBuyAmountLowest() {
        return buyAmountLowest;
    }

    public void setBuyAmountLowest(Double buyAmountLowest) {
        this.buyAmountLowest = buyAmountLowest;
    }

    public String getResourcesPath() {
        return resourcesPath;
    }

    public void setResourcesPath(String resourcesPath) {
        this.resourcesPath = resourcesPath;
    }

    private String getFileUploadPath() {
        if (StringUtils.isBlank(fileUploadPath)) {
            fileUploadPath = System.getProperty("user.home");
        }
        return fileUploadPath;
    }

    public void setFileUploadPath(String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }

    public Integer getLoginTokenCookieExpirationTime() {
        return loginTokenCookieExpirationTime;
    }

    public void setLoginTokenCookieExpirationTime(Integer loginTokenCookieExpirationTime) {
        this.loginTokenCookieExpirationTime = loginTokenCookieExpirationTime;
    }

    public String getLoginTokenCookieName() {
        return loginTokenCookieName;
    }

    public void setLoginTokenCookieName(String loginTokenCookieName) {
        this.loginTokenCookieName = loginTokenCookieName;
    }

    public String getLoginPictureSessionKey() {
        return loginPictureSessionKey;
    }

    public void setLoginPictureSessionKey(String loginPictureSessionKey) {
        this.loginPictureSessionKey = loginPictureSessionKey;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 存入key值
     *
     * @param name 模块名
     * @param key  唯一标识
     * @return 唯一标识
     */
    public String settingKey(String name, String key) {
        return getAppName() + ":" + name + key;
    }

    /**
     * 文件上传访问地址
     */
    private String fileUploadHost;

    private String getFileUploadHost() {
        return fileUploadHost;
    }

    public void setFileUploadHost(String fileUploadHost) {
        this.fileUploadHost = fileUploadHost;
    }

    /**
     * 获取文件上传最终目录
     *
     * @param context 请求对象
     * @return 目录
     */
    public String getUploadFinalDir(ServletContext context) {
        String fileUploadPath = getFileUploadPath();
        if (StringUtils.isBlank(fileUploadPath)) {
            fileUploadPath = RequestUtils.getProjectAbsolutePath(context).getPath();
        } else {
            if (!StringUtils.isBlank(getFileUploadUri())) {
                fileUploadPath += URLUtils.uriParseFilePath(getFileUploadUri());
            }
        }
        /*if (!StringUtils.isBlank(getResourcesPath())) {
            fileUploadPath += File.separator + getResourcesPath();
        }*/
        return fileUploadPath;
    }

    /**
     * 获取文件上传的域名基本地址
     *
     * @param request 请求对象
     * @return 域名基本地址
     */
    private String getUploadHost(HttpServletRequest request) {
        String host = getFileUploadHost();
        if (StringUtils.isBlank(host)) {
            host = RequestUtils.getHostPath(request);
        } else {
            if (!StringUtils.isBlank(getFileUploadUri())) {
                host += URLUtils.URL_SPLIT + getFileUploadUri();
            }
        }
        return host;
    }

    public String getUploadHost(HttpServletRequest request, FileType1 fileType, String path, String name) {
        fileType = FileType1.fileTypeInit(fileType);
        String uploadHost = getUploadHost(request);
        return URLUtils.urlMosaic(uploadHost, FileSeeController.getPath(fileType, path, name));
    }
}
