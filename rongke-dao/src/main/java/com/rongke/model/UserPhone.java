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
 * @UserPhone
 * @(user_phone)
 */
@TableName("user_phone")
public class UserPhone implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * @备注:
     * @字段:id BIGINT(19)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * @备注:
     * @字段:user_id BIGINT(19)
     */
    private Long userId;
    /**
     * 备注:
     */
    @TableField(exist = false)
    private User user;

    /**
     * @备注:
     * @字段:phone VARCHAR(255)
     */
    private String phone;

    private String message;

    /**
     * @备注:状态0未通过验证1通过
     * @字段:status INT(10)
     */
    private Integer status;

    /**
     * @备注:状态0未拉取1拉取成功
     * @字段:bill_status INT(10)
     */
    private Integer bill_status;

    /**
     * @备注:状态0未拉取1拉取成功
     * @字段:report_status INT(10)
     */
    private Integer report_status;

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
     * @备注:
     * @字段:real_name VARCHAR(255)
     */
    private String realName;

    /**
     * @备注:
     * @字段:identity_code VARCHAR(255)
     */
    private String identityCode;

    /**
     * @备注:
     * @字段:account_balance VARCHAR(255)
     */
    private String accountBalance;

    /**
     * @备注:
     * @字段:net_time VARCHAR(255)
     */
    private String netTime;

    /**
     * @备注:
     * @字段:net_ageo VARCHAR(255)
     */
    private String netAgeo;

    /**
     * @备注:
     * @字段:mobile_status VARCHAR(255)
     */
    private String mobileStatus;

    /**
     * @备注:
     * @字段:credit_level VARCHAR(255)
     */
    private String creditLevel;

    private String taskId;

    private String mxDto;

    public UserPhone() {
    }

    public UserPhone(
            Long id
    ) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getNetTime() {
        return netTime;
    }

    public void setNetTime(String netTime) {
        this.netTime = netTime;
    }

    public String getNetAgeo() {
        return netAgeo;
    }

    public void setNetAgeo(String netAgeo) {
        this.netAgeo = netAgeo;
    }

    public String getMobileStatus() {
        return mobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        this.mobileStatus = mobileStatus;
    }

    public String getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(String creditLevel) {
        this.creditLevel = creditLevel;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMxDto() {
        return mxDto;
    }

    public void setMxDto(String mxDto) {
        this.mxDto = mxDto;
    }

    public Integer getBill_status() {
        return bill_status;
    }

    public void setBill_status(Integer bill_status) {
        this.bill_status = bill_status;
    }

    public Integer getReport_status() {
        return report_status;
    }

    public void setReport_status(Integer report_status) {
        this.report_status = report_status;
    }
}
