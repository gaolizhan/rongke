package com.rongke.web.payutil.helipay.vo.response;


/**
 * Filename        TransferQueryResponVo.java
 * <p>
 * Description    代付单笔查询VO.
 * Copyright      Copyright (c) 2016-2022 All Rights Reserved.
 * Company       shuniao.com Inc Inc.
 *
 * @author 杨武
 * @version 1.0
 * @date 2018/11/10 12:44
 */
public class TransferQueryResponVo {

    private String rt1_bizType;
    private String rt2_retCode;
    private String rt3_retMsg;
    private String rt4_customerNumber;
    private String rt5_orderId;
    private String rt6_serialNumber;
    //打款状态
    private String rt7_orderStatus;
    //失败原因
    private String rt8_reason;
    private String sign;

    public String getRt1_bizType() {
        return rt1_bizType;
    }

    public void setRt1_bizType(String rt1_bizType) {
        this.rt1_bizType = rt1_bizType;
    }

    public String getRt2_retCode() {
        return rt2_retCode;
    }

    public void setRt2_retCode(String rt2_retCode) {
        this.rt2_retCode = rt2_retCode;
    }

    public String getRt3_retMsg() {
        return rt3_retMsg;
    }

    public void setRt3_retMsg(String rt3_retMsg) {
        this.rt3_retMsg = rt3_retMsg;
    }

    public String getRt4_customerNumber() {
        return rt4_customerNumber;
    }

    public void setRt4_customerNumber(String rt4_customerNumber) {
        this.rt4_customerNumber = rt4_customerNumber;
    }

    public String getRt5_orderId() {
        return rt5_orderId;
    }

    public void setRt5_orderId(String rt5_orderId) {
        this.rt5_orderId = rt5_orderId;
    }

    public String getRt6_serialNumber() {
        return rt6_serialNumber;
    }

    public void setRt6_serialNumber(String rt6_serialNumber) {
        this.rt6_serialNumber = rt6_serialNumber;
    }

    public String getRt7_orderStatus() {
        return rt7_orderStatus;
    }

    public void setRt7_orderStatus(String rt7_orderStatus) {
        this.rt7_orderStatus = rt7_orderStatus;
    }

    public String getRt8_reason() {
        return rt8_reason;
    }

    public void setRt8_reason(String rt8_reason) {
        this.rt8_reason = rt8_reason;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
