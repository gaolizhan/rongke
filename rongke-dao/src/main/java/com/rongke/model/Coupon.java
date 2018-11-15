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
 * @Coupon
 * @优惠券,红包(coupon)
 */
@TableName("coupon")
public class Coupon implements Serializable {
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
     * @备注:节约金额，默认0.00
     * @字段:save_money DECIMAL(10)
     */
    private java.math.BigDecimal saveMoney;


    /**
     * @备注:限制使用时间
     * @字段:limit_time DATE(10)
     */
    private java.util.Date limitTime;


    /**
     * @备注:默认0表示不限制，其他则表示每人领取限制
     * @字段:get_limit INT(10)
     */
    private java.lang.Integer getLimit;


    /**
     * @备注:优惠券总数
     * @字段:all_mount INT(10)
     */
    private java.lang.Integer allMount;


    /**
     * @备注:已使用的优惠券
     * @字段:use_mount INT(10)
     */
    private java.lang.Integer useMount;


    /**
     * @备注:已领取未使用的优惠券
     * @字段:get_mount INT(10)
     */
    private java.lang.Integer getMount;


    /**
     * @备注:优惠券状态，默认1表示正常，2表示暂停使用,3表示过期优惠券
     * @字段:coupon_status INT(10)
     */
    private java.lang.Integer couponStatus;

    /**
     * @备注:有效时长(天)
     * @字段:valid_time INT(10)
     */
    private java.lang.Integer validTime;

    /**
     * @备注:类型
     * @字段:type INT(10)
     */
    private java.lang.Integer type;


    /**
     * @备注:优惠券名称
     * @字段:coupou_name VARCHAR(255)
     */
    private java.lang.String coupouName;


    public Coupon() {
    }

    public Coupon(
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


    public void setSaveMoney(java.math.BigDecimal saveMoney) {
        this.saveMoney = saveMoney;
    }

    public java.math.BigDecimal getSaveMoney() {
        return this.saveMoney;
    }


    public void setLimitTime(java.util.Date limitTime) {
        this.limitTime = limitTime;
    }

    public java.util.Date getLimitTime() {
        return this.limitTime;
    }


    public void setGetLimit(java.lang.Integer getLimit) {
        this.getLimit = getLimit;
    }

    public java.lang.Integer getGetLimit() {
        return this.getLimit;
    }


    public void setAllMount(java.lang.Integer allMount) {
        this.allMount = allMount;
    }

    public java.lang.Integer getAllMount() {
        return this.allMount;
    }


    public void setUseMount(java.lang.Integer useMount) {
        this.useMount = useMount;
    }

    public java.lang.Integer getUseMount() {
        return this.useMount;
    }


    public void setGetMount(java.lang.Integer getMount) {
        this.getMount = getMount;
    }

    public java.lang.Integer getGetMount() {
        return this.getMount;
    }


    public void setCouponStatus(java.lang.Integer couponStatus) {
        this.couponStatus = couponStatus;
    }

    public java.lang.Integer getCouponStatus() {
        return this.couponStatus;
    }


    public void setCoupouName(java.lang.String coupouName) {
        this.coupouName = coupouName;
    }

    public java.lang.String getCoupouName() {
        return this.coupouName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getValidTime() {
        return validTime;
    }

    public void setValidTime(Integer validTime) {
        this.validTime = validTime;
    }
}
