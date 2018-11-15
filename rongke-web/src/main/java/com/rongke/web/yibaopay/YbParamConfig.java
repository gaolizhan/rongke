package com.rongke.web.yibaopay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:yibaourl.properties","classpath:merchantInfo.properties"})
public class YbParamConfig {

    @Value("${merchantno}")
    private String  merchantno;

    @Value("${appKey}")
    private String appKey;

    @Value("${terminalno}")
    private String terminalno;

    @Value("${yibao.url.firstPaymentUrl}")
    private String firstPaymentUrl;

    @Value("${yibao.url.firstPaymentCallBackUrl}")
    private String firstPaymentCallBackUrl;

    @Value("${yibao.url.firstPaymentConfirmUrl}")
    private String firstPaymentConfirmUrl;


    @Value("${yibao.url.unifiedPayUrl}")
    private String unifiedPayUrl;

    @Value("${yibao.url.unifiedPayCallBackUrl}")
    private String unifiedPayCallBackUrl;



    public String getMerchantno() {
        return merchantno;
    }

    public void setMerchantno(String merchantno) {
        this.merchantno = merchantno;
    }

    public String getFirstPaymentUrl() {
        return firstPaymentUrl;
    }

    public void setFirstPaymentUrl(String firstPaymentUrl) {
        this.firstPaymentUrl = firstPaymentUrl;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUnifiedPayUrl() {
        return unifiedPayUrl;
    }

    public void setUnifiedPayUrl(String unifiedPayUrl) {
        this.unifiedPayUrl = unifiedPayUrl;
    }

    public String getTerminalno() {
        return terminalno;
    }

    public void setTerminalno(String terminalno) {
        this.terminalno = terminalno;
    }

    public String getFirstPaymentConfirmUrl() {
        return firstPaymentConfirmUrl;
    }

    public void setFirstPaymentConfirmUrl(String firstPaymentConfirmUrl) {
        this.firstPaymentConfirmUrl = firstPaymentConfirmUrl;
    }

    public String getFirstPaymentCallBackUrl() {
        return firstPaymentCallBackUrl;
    }

    public void setFirstPaymentCallBackUrl(String firstPaymentCallBackUrl) {
        this.firstPaymentCallBackUrl = firstPaymentCallBackUrl;
    }

    public String getUnifiedPayCallBackUrl() {
        return unifiedPayCallBackUrl;
    }

    public void setUnifiedPayCallBackUrl(String unifiedPayCallBackUrl) {
        this.unifiedPayCallBackUrl = unifiedPayCallBackUrl;
    }
}
