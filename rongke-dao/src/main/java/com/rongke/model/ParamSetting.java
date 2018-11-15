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

/**
 * @version : Ver 1.0
 * @ParamSetting
 * @(param_setting)
 */
@TableName("param_setting")
public class ParamSetting implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * @备注:
     * @字段:id BIGINT(19)
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;


    /**
     * @备注:贷款期限
     * @字段:limit_days INT(10)
     */
    private java.lang.Integer limitDays;


    /**
     * @备注:贷款金额
     * @字段:min_borrow_money DECIMAL(33)
     */
    private java.math.BigDecimal minBorrowMoney;


    /**
     * @备注:
     * @字段:max_borrow_money DECIMAL(33)
     */
    private java.math.BigDecimal maxBorrowMoney;


    /**
     * @备注:利息 %
     * @字段:interest_percent DOUBLE(10)
     */
    private java.lang.Double interestPercent;


    /**
     * @备注:平台服务费
     * @字段:place_serve_percent DOUBLE(10)
     */
    private java.lang.Double placeServePercent;


    /**
     * @备注:信息认证费
     * @字段:msg_auth_percent DOUBLE(10)
     */
    private java.lang.Double msgAuthPercent;


    /**
     * @备注:风控服务费
     * @字段:risk_serve_percent DOUBLE(10)
     */
    private java.lang.Double riskServePercent;


    /**
     * @备注:风险准备金
     * @字段:risk_plan_percent DOUBLE(10)
     */
    private java.lang.Double riskPlanPercent;


    /**
     * @备注:容限期
     * @字段:allow_days INT(10)
     */
    private java.lang.Integer allowDays;


    /**
     * @备注:容限期日利率
     * @字段:allow_percent DOUBLE(10)
     */
    private java.lang.Double allowPercent;


    /**
     * @备注:逾期日利率
     * @字段:overdue_percent DOUBLE(10)
     */
    private java.lang.Double overduePercent;


    /**
     * @备注:
     * @字段:gmt_datetime DATETIME(19)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date gmtDatetime = new java.util.Date();


    /**
     * @备注:
     * @字段:upt_datetime DATETIME(19)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date uptDatetime = new java.util.Date();

    /**

     * @备注:状态
     * @字段:status INT(10)
     */
    private java.lang.Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public ParamSetting() {
    }

    public ParamSetting(
            java.lang.Long id
    ) {
        this.id = id;
    }


    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getId() {
        return this.id;
    }


    public void setLimitDays(java.lang.Integer limitDays) {
        this.limitDays = limitDays;
    }

    public java.lang.Integer getLimitDays() {
        return this.limitDays;
    }


    public void setMinBorrowMoney(java.math.BigDecimal minBorrowMoney) {
        this.minBorrowMoney = minBorrowMoney;
    }

    public java.math.BigDecimal getMinBorrowMoney() {
        return this.minBorrowMoney;
    }


    public void setMaxBorrowMoney(java.math.BigDecimal maxBorrowMoney) {
        this.maxBorrowMoney = maxBorrowMoney;
    }

    public java.math.BigDecimal getMaxBorrowMoney() {
        return this.maxBorrowMoney;
    }


    public void setInterestPercent(java.lang.Double interestPercent) {
        this.interestPercent = interestPercent;
    }

    public java.lang.Double getInterestPercent() {
        return this.interestPercent;
    }


    public void setPlaceServePercent(java.lang.Double placeServePercent) {
        this.placeServePercent = placeServePercent;
    }

    public java.lang.Double getPlaceServePercent() {
        return this.placeServePercent;
    }


    public void setMsgAuthPercent(java.lang.Double msgAuthPercent) {
        this.msgAuthPercent = msgAuthPercent;
    }

    public java.lang.Double getMsgAuthPercent() {
        return this.msgAuthPercent;
    }


    public void setRiskServePercent(java.lang.Double riskServePercent) {
        this.riskServePercent = riskServePercent;
    }

    public java.lang.Double getRiskServePercent() {
        return this.riskServePercent;
    }


    public void setRiskPlanPercent(java.lang.Double riskPlanPercent) {
        this.riskPlanPercent = riskPlanPercent;
    }

    public java.lang.Double getRiskPlanPercent() {
        return this.riskPlanPercent;
    }


    public void setAllowDays(java.lang.Integer allowDays) {
        this.allowDays = allowDays;
    }

    public java.lang.Integer getAllowDays() {
        return this.allowDays;
    }


    public void setAllowPercent(java.lang.Double allowPercent) {
        this.allowPercent = allowPercent;
    }

    public java.lang.Double getAllowPercent() {
        return this.allowPercent;
    }


    public void setOverduePercent(java.lang.Double overduePercent) {
        this.overduePercent = overduePercent;
    }

    public java.lang.Double getOverduePercent() {
        return this.overduePercent;
    }


    public void setGmtDatetime(java.util.Date gmtDatetime) {
        this.gmtDatetime = gmtDatetime;
    }

    public java.util.Date getGmtDatetime() {
        return this.gmtDatetime;
    }


    public void setUptDatetime(java.util.Date uptDatetime) {
        this.uptDatetime = uptDatetime;
    }

    public java.util.Date getUptDatetime() {
        return this.uptDatetime;
    }
}
