package com.rongke.model;

public class ControllerBaseVO {

    /**
     * 机构id
     */
    private String merchantId;

    /**
     *风控系统分配给产品的编号
     */
    private String productId;

    /**
     * 访问标识
     */
    String appKey;

    /**
     * 时间戳
     */
    String timeStamp;

    /**
     * 签名结果
     */
    String sign;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
