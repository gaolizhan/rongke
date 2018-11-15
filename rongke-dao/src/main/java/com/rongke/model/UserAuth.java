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
 * @UserAuth
 * @(user_auth)
 */
@TableName("user_auth")
public class UserAuth implements Serializable {
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


    /**
     * @备注:基本信息是否已验证 0否1是
     * @字段:baisc_auth INT(10)
     */
    private java.lang.Integer baiscAuth;


    /**
     * 认证费用是否已缴
     */
    private Integer authFee;


    /**
     * @备注:银行是否验证0否1是
     * @字段:bank_auth INT(10)
     */
    private java.lang.Integer bankAuth;


    /**
     * @备注:手机是否认证 0否1是
     * @字段:phone_auth INT(10)
     */
    private java.lang.Integer phoneAuth;


    /**
     * @备注:身份是否验证
     * @字段:identity_auth INT(10)
     */
    private java.lang.Integer identityAuth;


    /**
     * @备注:芝麻是否验证
     * @字段:zhima_auth INT(10)
     */
    private java.lang.Integer zhimaAuth;


    /**
     * @备注:社保是否验证
     * @字段:shebao_auth INT(10)
     */
    private java.lang.Integer shebaoAuth;


    /**
     * @备注:公积金是否验证
     * @字段:gongjijin_auth INT(10)
     */
    private java.lang.Integer gongjijinAuth;


    /**
     * @备注:支付宝是否验证
     * @字段:zhifubao_auth INT(10)
     */
    private java.lang.Integer zhifubaoAuth;


    /**
     * @备注:京东是否验证
     * @字段:jindong_auth INT(10)
     */
    private java.lang.Integer jindongAuth;


    /**
     * @备注:淘宝是否认证
     * @字段:taobao_auth INT(10)
     */
    private java.lang.Integer taobaoAuth;


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


    public UserAuth() {
    }

    public UserAuth(
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


    public Integer getAuthFee() {
        return authFee;
    }

    public void setAuthFee(Integer authFee) {
        this.authFee = authFee;
    }

    public void setBaiscAuth(java.lang.Integer baiscAuth) {
        this.baiscAuth = baiscAuth;
    }

    public java.lang.Integer getBaiscAuth() {
        return this.baiscAuth;
    }


    public void setBankAuth(java.lang.Integer bankAuth) {
        this.bankAuth = bankAuth;
    }

    public java.lang.Integer getBankAuth() {
        return this.bankAuth;
    }


    public void setPhoneAuth(java.lang.Integer phoneAuth) {
        this.phoneAuth = phoneAuth;
    }

    public java.lang.Integer getPhoneAuth() {
        return this.phoneAuth;
    }


    public void setIdentityAuth(java.lang.Integer identityAuth) {
        this.identityAuth = identityAuth;
    }

    public java.lang.Integer getIdentityAuth() {
        return this.identityAuth;
    }


    public void setZhimaAuth(java.lang.Integer zhimaAuth) {
        this.zhimaAuth = zhimaAuth;
    }

    public java.lang.Integer getZhimaAuth() {
        return this.zhimaAuth;
    }


    public void setShebaoAuth(java.lang.Integer shebaoAuth) {
        this.shebaoAuth = shebaoAuth;
    }

    public java.lang.Integer getShebaoAuth() {
        return this.shebaoAuth;
    }


    public void setGongjijinAuth(java.lang.Integer gongjijinAuth) {
        this.gongjijinAuth = gongjijinAuth;
    }

    public java.lang.Integer getGongjijinAuth() {
        return this.gongjijinAuth;
    }


    public void setZhifubaoAuth(java.lang.Integer zhifubaoAuth) {
        this.zhifubaoAuth = zhifubaoAuth;
    }

    public java.lang.Integer getZhifubaoAuth() {
        return this.zhifubaoAuth;
    }


    public void setJindongAuth(java.lang.Integer jindongAuth) {
        this.jindongAuth = jindongAuth;
    }

    public java.lang.Integer getJindongAuth() {
        return this.jindongAuth;
    }


    public void setTaobaoAuth(java.lang.Integer taobaoAuth) {
        this.taobaoAuth = taobaoAuth;
    }

    public java.lang.Integer getTaobaoAuth() {
        return this.taobaoAuth;
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
