package com.rongke.model;

public class PreApplyWithCreditVO extends ControllerBaseVO {
    /**
     *  资产方订单编号
     */
    private String orderId;

    /**
     * 用户姓名
     */
    private String userName;


    /**
     * 身份证号
     */
    private String cardNum;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 进件数据
     */
    private RiskDataDTO riskData;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public RiskDataDTO getRiskData() {
        return riskData;
    }

    public void setRiskData(RiskDataDTO riskData) {
        this.riskData = riskData;
    }
}