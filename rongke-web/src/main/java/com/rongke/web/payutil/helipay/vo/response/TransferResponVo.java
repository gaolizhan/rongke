package com.rongke.web.payutil.helipay.vo.response;

/**
 * TODO 类实现描述 
 * @Company 杭州木瓜科技有限公司
 * @className: com.rongke.web.payutil.helipay.vo.response
 * @author songlan@dianjia.com 
 * @date 2018/10/20. 11:37 PM
 */
public class TransferResponVo {
    private String  rt2_retCode;
    private  String sign;
    private String rt1_bizType;
    private  String  rt5_orderId;
    private String  rt4_customerNumber;
    private String  rt3_retMsg;
    private String rt6_serialNumber;

    public String getRt2_retCode() {
        return rt2_retCode;
    }

    public void setRt2_retCode(String rt2_retCode) {
        this.rt2_retCode = rt2_retCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getRt1_bizType() {
        return rt1_bizType;
    }

    public void setRt1_bizType(String rt1_bizType) {
        this.rt1_bizType = rt1_bizType;
    }

    public String getRt5_orderId() {
        return rt5_orderId;
    }

    public void setRt5_orderId(String rt5_orderId) {
        this.rt5_orderId = rt5_orderId;
    }

    public String getRt4_customerNumber() {
        return rt4_customerNumber;
    }

    public void setRt4_customerNumber(String rt4_customerNumber) {
        this.rt4_customerNumber = rt4_customerNumber;
    }

    public String getRt3_retMsg() {
        return rt3_retMsg;
    }

    public void setRt3_retMsg(String rt3_retMsg) {
        this.rt3_retMsg = rt3_retMsg;
    }

    public String getRt6_serialNumber() {
        return rt6_serialNumber;
    }

    public void setRt6_serialNumber(String rt6_serialNumber) {
        this.rt6_serialNumber = rt6_serialNumber;
    }
}
