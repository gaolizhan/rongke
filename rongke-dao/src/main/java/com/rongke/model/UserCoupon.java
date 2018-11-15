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
import java.util.Date;

/**
 * @version : Ver 1.0
 * @UserCoupon
 * @用户优惠券列(user_coupon)
 */
@TableName("user_coupon")
public class UserCoupon implements Serializable {
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
    private java.util.Date uptDatetime;


    /**
     * @备注:
     * @字段:past_datetime DATETIME(19)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date pastDatetime;


    /**
     * @备注:用户id
     * @字段:user_id BIGINT(19)
     */
    private java.lang.Long userId;
    /**
     * 备注:用户id
     */
    @TableField(exist = false)
    private User user;


    /**
     * @备注:优惠券id
     * @字段:coupon_id BIGINT(19)
     */
    private java.lang.Long couponId;
    /**
     * 备注:优惠券id
     */
    @TableField(exist = false)
    private Coupon coupon;

    /**
     * 过期优惠券user数量
     */
    @TableField(exist = false)
    private Integer userCount;

    /**
     * @备注:节约金额
     * @字段:save_money DECIMAL(10)
     */
    private java.math.BigDecimal saveMoney;

    /**
     * @备注:被邀请人手机号
     * @字段:invitee_phone VARCHAR(255)
     */
    private java.lang.String inviteePhone;

    /**
     * @备注:默认1表示正常，2表示已使用，3表示过期，4表示不能使用
     * @字段:status INT(10)
     */
    private java.lang.Integer status;


    public UserCoupon() {
    }

    public UserCoupon(
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

    public void setCouponId(java.lang.Long couponId) {
        this.couponId = couponId;
    }

    public java.lang.Long getCouponId() {
        return this.couponId;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Coupon getCoupon() {
        return this.coupon;
    }

    public void setSaveMoney(java.math.BigDecimal saveMoney) {
        this.saveMoney = saveMoney;
    }

    public java.math.BigDecimal getSaveMoney() {
        return this.saveMoney;
    }

    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    public java.lang.Integer getStatus() {
        return this.status;
    }

    public Date getPastDatetime() {
        return pastDatetime;
    }

    public void setPastDatetime(Date pastDatetime) {
        this.pastDatetime = pastDatetime;
    }

    public String getInviteePhone() {
        return inviteePhone;
    }

    public void setInviteePhone(String inviteePhone) {
        this.inviteePhone = inviteePhone;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
}
