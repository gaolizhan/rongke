package com.rongke.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rongke.utils.DateJsonDeserializer;
import com.rongke.utils.DateJsonSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version : Ver 1.0
 * @LoanOrder
 * @(loan_order)
 */
@TableName("loan_order")
public class LoanOrder implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * @备注:
     * @字段:id BIGINT(19)
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;


    /**
     * @备注:
     * @字段:user_id BIGINT(19)
     */
    private java.lang.Long userId;
    /**
     * 备注:
     */
    @TableField(exist = false)
    private User user;

    public double getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(double totalPeople) {
        this.totalPeople = totalPeople;
    }

    /**
     * 总人数
     */
    private transient double totalPeople;

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    /**
     * 总金额

     */
    private  transient double totalMoney;


    /**
     * @备注:同盾分数
     * @字段:td_score VARCHAR(55)
     */
    private String tdScore;


    /**
     * @备注:
     * @字段:param_setting_id BIGINT(19)
     */
    private java.lang.Long paramSettingId;
    /**
     * 备注:
     */
    @TableField(exist = false)
    private ParamSetting paramSetting;


    /**
     * @备注:订单号码
     * @字段:order_number VARCHAR(255)
     */
    private java.lang.String orderNumber;

    /**
     * @备注:银行卡号
     * @字段:bank_card_num VARCHAR(255)
     */
    private java.lang.String bankCardNum;



    /**
     * @备注:同盾风险项
     * @字段:risk_items TEXT(65535)
     */
    private String riskItems;


    /**
     * @备注:银行名称
     * @字段:bank_name VARCHAR(255)
     */
    private java.lang.String bankName;

    /**
     * @备注:借款期限（天）
     * @字段:limit_days INT(10)
     */
    private java.lang.Integer limitDays;


    /**
     * @备注:借款金额
     * @字段:borrow_money DECIMAL(33)
     */
    private java.math.BigDecimal borrowMoney;


    /**@备注：协商还款
     * @字段：consult_repayment_money  DECIMAL(33)
     *
     */
    private java.math.BigDecimal consultRepaymentMoney;

    public BigDecimal getConsultRepaymentMoney() {
        return consultRepaymentMoney;
    }

    public void setConsultRepaymentMoney(BigDecimal consultRepaymentMoney) {
        this.consultRepaymentMoney = consultRepaymentMoney;
    }

    /**
     * @备注:到账金额
     * @字段:real_money DECIMAL(33)
     */
    private java.math.BigDecimal realMoney;


    /**
     * @备注:利息
     * @字段:interest_money DECIMAL(33)
     */
    private java.math.BigDecimal interestMoney;


    /**
     * @备注:平台服务费
     * @字段:place_serve_money DECIMAL(33)
     */
    private java.math.BigDecimal placeServeMoney;


    /**
     * @备注:信息认证费
     * @字段:msg_auth_money DECIMAL(33)
     */
    private java.math.BigDecimal msgAuthMoney;


    /**
     * @备注:风控服务费
     * @字段:risk_serve_money DECIMAL(33)
     */
    private java.math.BigDecimal riskServeMoney;


    /**
     * @备注:风险准备金
     * @字段:risk_plan_money DECIMAL(33)
     */
    private java.math.BigDecimal riskPlanMoney;


    /**
     * @备注:综合费用
     * @字段:wate_money DECIMAL(33)
     */
    private java.math.BigDecimal wateMoney;


    /**
     * @备注:优惠卷节省金额
     * @字段:save_money DECIMAL(33)
     */
    private java.math.BigDecimal saveMoney;


    /**
     * @备注:应还金额
     * @字段:need_pay_money DECIMAL(33)
     */
    private java.math.BigDecimal needPayMoney;


    /**
     * @备注:实还金额
     * @字段:real_pay_money DECIMAL(33)
     */
    private java.math.BigDecimal realPayMoney;

    /**
     * @备注:利率
     * @字段:interest_precent DOUBLE(33)
     */
    private java.lang.Double interestPrecent;


    /**
     * @备注:借款时间
     * @字段:gmt_datetime DATETIME(19)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date gmtDatetime = new java.util.Date();


    /**
     * @备注:
     * @字段:upt_dateime DATETIME(19)
     */
    private java.util.Date uptDateime;


    /**
     * @备注:放款时间
     * @字段:pass_time DATETIME(19)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date passTime;


    /**
     * @备注:打款时间
     * @字段:give_time DATETIME(19)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date giveTime;


    /**
     * @备注:应还款时间时间
     * @字段:limit_pay_time DATE(10)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date limitPayTime;

    /**
     * @备注:逾期时间
     * @字段:overdue_time DATE(10)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date overdueTime;


    /**
     * @备注:实际还款时间
     * @字段:real_pay_time DATETIME(19)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date realPayTime;


    /**
     * @备注:
     * @字段:user_coupon_id BIGINT(19)
     */
    private java.lang.Long userCouponId;
    /**
     * 备注:
     */
    @TableField(exist = false)
    private UserCoupon userCoupon;


    /**
     * @备注:逾期天数
     * @字段:overdue_days INT(10)
     */
    private java.lang.Integer overdueDays;

    /**
     * @备注:容限天数
     * @字段:allow_days INT(10)
     */
    private java.lang.Integer allowDays;

    /**
     * @备注:逾期金额
     * @字段:overdue_money DECIMAL(33)
     */
    private java.math.BigDecimal overdueMoney;


    /**
     * @备注:容限期费用
     * @字段:allow_money DECIMAL(33)
     */
    private java.math.BigDecimal allowMoney;


    /**
     * @备注:放款员id
     * @字段:auditor_id BIGINT(19)
     */
    private java.lang.Long auditorId;
    /**
     * 备注:
     */
    @TableField(exist = false)
    private Admin admin;


    /**
     * @备注:订单状态 默认0未申请    1审核中2待打款3待还款4容限期中5已逾期6已还款7审核失败8坏账
     * @字段:order_status INT(10)
     */
    private java.lang.Integer orderStatus;


    /**
     * @备注:还款状态 1正常还款2逾期还款
     * @字段:pay_status INT(10)
     */
    private java.lang.Integer payStatus;

    /**
     * @备注:打款状态
     * @字段:give_status INT(10)
     */
    private java.lang.Integer giveStatus;

    /**
     * @备注:借款协议
     * @字段:agreement_url VARCHAR(255)
     */
    private java.lang.String agreementUrl;

    /**
     * @备注:借款服务
     * @字段:agreement_two_url VARCHAR(255)
     */
    private java.lang.String agreementTwoUrl;

    /**
     * @备注:会员协议
     * @字段:agreement_two_url VARCHAR(500)
     */
    private java.lang.String agreementThirdUrl;

    /**
     * @备注:
     * @字段:lian_pay_num VARCHAR(255)
     */
    private java.lang.String lianPayNum;

    /**
     * @备注:
     * @字段:lian_repay_num VARCHAR(255)
     */
    private java.lang.String lianRepayNum;

    /**
     * @备注:
     * @字段:no_agree VARCHAR(255)
     */
    private java.lang.String noAgree;

    /**
     * @备注:
     * @字段:repayment_no VARCHAR(255)
     */
    private java.lang.String repaymentNo;

    /**
     * @备注:打款状态
     * @字段:extend_num INT(33)
     */
    private java.lang.Integer extendNum;

    /**
     * @备注
     * @字段:channel_profit DECIMAL(33)
     */
    private java.math.BigDecimal channelProfit;


    /**
     * 支付请求号码
     */
    private String requestNo;

    /**
     * 1,续期2还款
     */
    private Integer type;

    /**
     * 0:未续期 1:被续期 2:续期'
     */
    private  Integer extendType;

    /**
     * @备注:借款时间
     * @字段:gmt_datetime DATETIME(19)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date lastPressDate = new java.util.Date();

    private Integer pressTimes;

    @TableField(exist = false)
    private String channelName;

    private BigDecimal autoPaymentMoney;

    @TableField(exist = false)
    private String pressMoneyManName;

    private BigDecimal belowPartPayment;

    @TableField(exist = false)
    private Boolean extendSign;

    public BigDecimal getBelowPartPayment() {
        return belowPartPayment;
    }

    public void setBelowPartPayment(BigDecimal belowPartPayment) {
        this.belowPartPayment = belowPartPayment;
    }

    public BigDecimal getAutoPaymentMoney() {
        return autoPaymentMoney;
    }

    public void setAutoPaymentMoney(BigDecimal autoPaymentMoney) {
        this.autoPaymentMoney = autoPaymentMoney;
    }

    public LoanOrder() {
    }

    public LoanOrder(
            java.lang.Long id
    ) {
        this.id = id;
    }

    private Long pressMoneyMan;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Long getPressMoneyMan() {
        return pressMoneyMan;
    }

    public void setPressMoneyMan(Long pressMoneyMan) {
        this.pressMoneyMan = pressMoneyMan;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getId() {
        return this.id;
    }

    public void setUserId(java.lang.Long userId) {
        this.userId = userId;
    }

    public java.lang.Long getUserId() {
        return this.userId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setParamSettingId(java.lang.Long paramSettingId) {
        this.paramSettingId = paramSettingId;
    }

    public java.lang.Long getParamSettingId() {
        return this.paramSettingId;
    }

    public void setParamSetting(ParamSetting paramSetting) {
        this.paramSetting = paramSetting;
    }

    public ParamSetting getParamSetting() {
        return this.paramSetting;
    }


    public void setOrderNumber(java.lang.String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public java.lang.String getOrderNumber() {
        return this.orderNumber;
    }


    public void setLimitDays(java.lang.Integer limitDays) {
        this.limitDays = limitDays;
    }

    public java.lang.Integer getLimitDays() {
        return this.limitDays;
    }


    public void setBorrowMoney(java.math.BigDecimal borrowMoney) {
        this.borrowMoney = borrowMoney;
    }

    public java.math.BigDecimal getBorrowMoney() {
        return this.borrowMoney;
    }


    public void setRealMoney(java.math.BigDecimal realMoney) {
        this.realMoney = realMoney;
    }

    public java.math.BigDecimal getRealMoney() {
        return this.realMoney;
    }

    public String getTdScore() {
        return tdScore;
    }

    public void setTdScore(String tdScore) {
        this.tdScore = tdScore;
    }

    public void setInterestMoney(java.math.BigDecimal interestMoney) {
        this.interestMoney = interestMoney;
    }

    public java.math.BigDecimal getInterestMoney() {
        return this.interestMoney;
    }


    public void setPlaceServeMoney(java.math.BigDecimal placeServeMoney) {
        this.placeServeMoney = placeServeMoney;
    }

    public java.math.BigDecimal getPlaceServeMoney() {
        return this.placeServeMoney;
    }


    public String getRiskItems() {
        return riskItems;
    }

    public void setRiskItems(String riskItems) {
        this.riskItems = riskItems;
    }

    public void setMsgAuthMoney(java.math.BigDecimal msgAuthMoney) {
        this.msgAuthMoney = msgAuthMoney;
    }

    public java.math.BigDecimal getMsgAuthMoney() {
        return this.msgAuthMoney;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setRiskServeMoney(java.math.BigDecimal riskServeMoney) {
        this.riskServeMoney = riskServeMoney;
    }

    public java.math.BigDecimal getRiskServeMoney() {
        return this.riskServeMoney;
    }


    public void setRiskPlanMoney(java.math.BigDecimal riskPlanMoney) {
        this.riskPlanMoney = riskPlanMoney;
    }

    public java.math.BigDecimal getRiskPlanMoney() {
        return this.riskPlanMoney;
    }


    public void setWateMoney(java.math.BigDecimal wateMoney) {
        this.wateMoney = wateMoney;
    }

    public java.math.BigDecimal getWateMoney() {
        return this.wateMoney;
    }


    public void setSaveMoney(java.math.BigDecimal saveMoney) {
        this.saveMoney = saveMoney;
    }

    public java.math.BigDecimal getSaveMoney() {
        return this.saveMoney;
    }


    public void setNeedPayMoney(java.math.BigDecimal needPayMoney) {
        this.needPayMoney = needPayMoney;
    }

    public java.math.BigDecimal getNeedPayMoney() {
        return this.needPayMoney;
    }


    public void setRealPayMoney(java.math.BigDecimal realPayMoney) {
        this.realPayMoney = realPayMoney;
    }

    public java.math.BigDecimal getRealPayMoney() {
        return this.realPayMoney;
    }


    public void setGmtDatetime(java.util.Date gmtDatetime) {
        this.gmtDatetime = gmtDatetime;
    }

    public java.util.Date getGmtDatetime() {
        return this.gmtDatetime;
    }


    public void setUptDateime(java.util.Date uptDateime) {
        this.uptDateime = uptDateime;
    }

    public java.util.Date getUptDateime() {
        return this.uptDateime;
    }


    public void setPassTime(java.util.Date passTime) {
        this.passTime = passTime;
    }

    public java.util.Date getPassTime() {
        return this.passTime;
    }


    public void setGiveTime(java.util.Date giveTime) {
        this.giveTime = giveTime;
    }

    public java.util.Date getGiveTime() {
        return this.giveTime;
    }


    public void setLimitPayTime(java.util.Date limitPayTime) {
        this.limitPayTime = limitPayTime;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public java.util.Date getLimitPayTime() {
        return this.limitPayTime;
    }


    public void setRealPayTime(java.util.Date realPayTime) {
        this.realPayTime = realPayTime;
    }

    public java.util.Date getRealPayTime() {
        return this.realPayTime;
    }

    public void setUserCouponId(java.lang.Long userCouponId) {
        this.userCouponId = userCouponId;
    }

    public java.lang.Long getUserCouponId() {
        return this.userCouponId;
    }

    public void setUserCoupon(UserCoupon userCoupon) {
        this.userCoupon = userCoupon;
    }

    public UserCoupon getUserCoupon() {
        return this.userCoupon;
    }


    public void setOverdueDays(java.lang.Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public java.lang.Integer getOverdueDays() {
        return this.overdueDays;
    }


    public void setOverdueMoney(java.math.BigDecimal overdueMoney) {
        this.overdueMoney = overdueMoney;
    }

    public java.math.BigDecimal getOverdueMoney() {
        return this.overdueMoney;
    }


    public void setAllowMoney(java.math.BigDecimal allowMoney) {
        this.allowMoney = allowMoney;
    }

    public java.math.BigDecimal getAllowMoney() {
        return this.allowMoney;
    }


    public void setAuditorId(java.lang.Long auditorId) {
        this.auditorId = auditorId;
    }

    public java.lang.Long getAuditorId() {
        return this.auditorId;
    }


    public void setOrderStatus(java.lang.Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public java.lang.Integer getOrderStatus() {
        return this.orderStatus;
    }


    public void setPayStatus(java.lang.Integer payStatus) {
        this.payStatus = payStatus;
    }

    public java.lang.Integer getPayStatus() {
        return this.payStatus;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Date getOverdueTime() {
        return overdueTime;
    }

    public void setOverdueTime(Date overdueTime) {
        this.overdueTime = overdueTime;
    }

    public String getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getAllowDays() {
        return allowDays;
    }

    public void setAllowDays(Integer allowDays) {
        this.allowDays = allowDays;
    }

    public String getAgreementUrl() {
        return agreementUrl;
    }

    public void setAgreementUrl(String agreementUrl) {
        this.agreementUrl = agreementUrl;
    }

    public String getAgreementTwoUrl() {
        return agreementTwoUrl;
    }

    public void setAgreementTwoUrl(String agreementTwoUrl) {
        this.agreementTwoUrl = agreementTwoUrl;
    }

    public Double getInterestPrecent() {
        return interestPrecent;
    }

    public void setInterestPrecent(Double interestPrecent) {
        this.interestPrecent = interestPrecent;
    }

    public Integer getGiveStatus() {
        return giveStatus;
    }

    public void setGiveStatus(Integer giveStatus) {
        this.giveStatus = giveStatus;
    }

    public String getLianPayNum() {
        return lianPayNum;
    }

    public void setLianPayNum(String lianPayNum) {
        this.lianPayNum = lianPayNum;
    }

    public String getLianRepayNum() {
        return lianRepayNum;
    }

    public void setLianRepayNum(String lianRepayNum) {
        this.lianRepayNum = lianRepayNum;
    }

    public String getNoAgree() {
        return noAgree;
    }

    public void setNoAgree(String noAgree) {
        this.noAgree = noAgree;
    }

    public String getRepaymentNo() {
        return repaymentNo;
    }

    public void setRepaymentNo(String repaymentNo) {
        this.repaymentNo = repaymentNo;
    }

    public Integer getExtendNum() {
        return extendNum;
    }

    public void setExtendNum(Integer extendNum) {
        this.extendNum = extendNum;
    }

    public BigDecimal getChannelProfit() {
        return channelProfit;
    }

    public void setChannelProfit(BigDecimal channelProfit) {
        this.channelProfit = channelProfit;
    }

    public Date getLastPressDate() {
        return lastPressDate;
    }

    public void setLastPressDate(Date lastPressDate) {
        this.lastPressDate = lastPressDate;
    }

    public Integer getPressTimes() {
        return pressTimes;
    }

    public void setPressTimes(Integer pressTimes) {
        this.pressTimes = pressTimes;
    }

    public String getAgreementThirdUrl() {
        return agreementThirdUrl;
    }

    public void setAgreementThirdUrl(String agreementThirdUrl) {
        this.agreementThirdUrl = agreementThirdUrl;
    }

    public String getPressMoneyManName() {
        return pressMoneyManName;
    }

    public void setPressMoneyManName(String pressMoneyManName) {
        this.pressMoneyManName = pressMoneyManName;
    }

    public Boolean getExtendSign() {
        return extendSign;
    }

    public void setExtendSign(Boolean extendSign) {
        this.extendSign = extendSign;
    }

    public Integer getExtendType() {
        return extendType;
    }

    public void setExtendType(Integer extendType) {
        this.extendType = extendType;
    }
}
