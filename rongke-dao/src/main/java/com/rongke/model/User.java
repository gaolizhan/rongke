package com.rongke.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rongke.utils.DateJsonDeserializer;
import com.rongke.utils.DateJsonSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @version : Ver 1.0
 * @User
 * @用户基本信息(user)
 */
@TableName("user")
public class User implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * @备注:
     * @字段:id BIGINT(19)
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 登录状态 0：去贷款页面  1：去商城页面
     */
    @TableField(exist = false)
    private String loginStatus;

    @TableField(exist = false)
    private boolean  onceOverdueSign;

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
     * @字段:uuid VARCHAR(32)
     */
    @Column(updatable = false, columnDefinition = "VARCHAR(32) COMMENT ''")
    private java.lang.String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "");

    private Integer isXuqi;



    /**
     * @备注:用户名
     * @字段:user_name VARCHAR(128)
     */
    private java.lang.String userName;

    private Integer isOld;

    public Integer getIsOld() {
        return isOld;
    }

    public void setIsOld(Integer isOld) {
        this.isOld = isOld;
    }

    /**
     * @备注:密码
     * @字段:password VARCHAR(45)
     */
    private java.lang.String password;


    /**
     * @备注:头像
     * @字段:head_img VARCHAR(256)
     */
    private java.lang.String headImg;


    /**
     * @备注:手机
     * @字段:phone VARCHAR(45)
     */
    private java.lang.String phone;


    /**
     * @备注:额度
     * @字段:money DECIMAL(33)
     */
    private java.math.BigDecimal money;


    /**
     * @备注:用户类型1普通会员
     * @字段:user_type INT(10)
     */
    private java.lang.Integer userType;


    /**
     * @备注:认证状态，默认0表示未认证，1表示已认证可借款
     * @字段:auth_status INT(10)
     */
    private java.lang.Integer authStatus;


    /**
     * @备注:
     * @字段:token VARCHAR(256)
     */
    private java.lang.String token;


    /**
     * @备注:默认1表示正常，2表示黑名单，3表示删除
     * @字段:status INT(10)
     */
    private java.lang.Integer status;


    /**
     * @备注:支付密码
     * @字段:pay_pwd VARCHAR(45)
     */
    private java.lang.String payPwd;


    /**
     * @备注:认证分数
     * @字段:auth_score INT(10)
     */
    private java.lang.Integer authScore;

    /**
     * @备注:优惠券总数
     * @字段:coupon_all_count INT(10)
     */
    private java.lang.Integer couponAllCount;

    /**
     * @备注:过期数
     * @字段:coupon_past_count INT(10)
     */
    private java.lang.Integer couponPastCount;

    /**
     * @备注:使用总数
     * @字段:coupon_use_count INT(10)
     */
    private java.lang.Integer couponUseCount;

    /**
     * @备注:设备标识
     * @字段:phone_sign VARCHAR(255)
     */
    private java.lang.String phoneSign;


    /**
     * @备注:
     * @字段:upt_datetime DATETIME(19)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date refuseRemoveTime;


    /**
     * @备注:是否已还款默认0是1否
     * @字段:is_pay INT(10)
     */
    private java.lang.Integer isPay;


    /**
     * 备注:
     */
    @TableField(exist = false)
    private java.lang.String idNo;

    /**
     * @备注:
     * @字段:bankauth_num INT(10)
     */
    private java.lang.Integer bankauthNum;

    /**
     * @备注:
     * @字段:channel_id BIGINT(33)
     */
    private java.lang.Long channelId;

    /**
     * 备注:
     */
    @TableField(exist = false)
    private Channel channel;

    /**
     * @备注:
     * @字段:channel_name VARCHAR(255)
     */
    private java.lang.String channelName;

    /**
     * 逾期次数
     */
    private Integer overdueTimes;
    /**
     * 借款次数
     */
    private Integer loanTimes;
    /**
     * 催款次数
     */
    private Integer pressTimes;


    /**
     * 信审员id
     */
    private Long adminAuditId;


    @TableField(exist = false)
    private UserZhifubao userZhifubao;


    @TableField(exist = false)
    private UserAuth userAuth;



    public User() {
    }

    public User(
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


    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }

    public java.lang.String getUuid() {
        return this.uuid;
    }


    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    public java.lang.String getUserName() {
        return this.userName;
    }


    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    public java.lang.String getPassword() {
        return this.password;
    }


    public void setHeadImg(java.lang.String headImg) {
        this.headImg = headImg;
    }

    public java.lang.String getHeadImg() {
        return this.headImg;
    }


    public void setPhone(java.lang.String phone) {
        this.phone = phone;
    }

    public java.lang.String getPhone() {
        return this.phone;
    }


    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }

    public java.math.BigDecimal getMoney() {
        return this.money;
    }


    public void setUserType(java.lang.Integer userType) {
        this.userType = userType;
    }

    public java.lang.Integer getUserType() {
        return this.userType;
    }


    public void setAuthStatus(java.lang.Integer authStatus) {
        this.authStatus = authStatus;
    }

    public java.lang.Integer getAuthStatus() {
        return this.authStatus;
    }


    public void setToken(java.lang.String token) {
        this.token = token;
    }

    public java.lang.String getToken() {
        return this.token;
    }


    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    public java.lang.Integer getStatus() {
        return this.status;
    }


    public void setPayPwd(java.lang.String payPwd) {
        this.payPwd = payPwd;
    }

    public java.lang.String getPayPwd() {
        return this.payPwd;
    }


    public void setAuthScore(java.lang.Integer authScore) {
        this.authScore = authScore;
    }

    public java.lang.Integer getAuthScore() {
        return this.authScore;
    }

    public void setPhoneSign(java.lang.String phoneSign) {
        this.phoneSign = phoneSign;
    }

    public java.lang.String getPhoneSign() {
        return this.phoneSign;
    }

    public void setIsPay(java.lang.Integer isPay) {
        this.isPay = isPay;
    }

    public java.lang.Integer getIsPay() {
        return this.isPay;
    }

    public Integer getCouponAllCount() {
        return couponAllCount;
    }

    public void setCouponAllCount(Integer couponAllCount) {
        this.couponAllCount = couponAllCount;
    }

    public Integer getCouponPastCount() {
        return couponPastCount;
    }

    public void setCouponPastCount(Integer couponPastCount) {
        this.couponPastCount = couponPastCount;
    }

    public Integer getCouponUseCount() {
        return couponUseCount;
    }

    public void setCouponUseCount(Integer couponUseCount) {
        this.couponUseCount = couponUseCount;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public Date getRefuseRemoveTime() {
        return refuseRemoveTime;
    }

    public void setRefuseRemoveTime(Date refuseRemoveTime) {
        this.refuseRemoveTime = refuseRemoveTime;
    }

    public Integer getBankauthNum() {
        return bankauthNum;
    }

    public void setBankauthNum(Integer bankauthNum) {
        this.bankauthNum = bankauthNum;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Integer getIsXuqi() {
        return isXuqi;
    }

    public void setIsXuqi(Integer isXuqi) {
        this.isXuqi = isXuqi;
    }

    public Integer getOverdueTimes() {
        return overdueTimes;
    }

    public void setOverdueTimes(Integer overdueTimes) {
        this.overdueTimes = overdueTimes;
    }

    public Integer getLoanTimes() {
        return loanTimes;
    }

    public void setLoanTimes(Integer loanTimes) {
        this.loanTimes = loanTimes;
    }

    public Integer getPressTimes() {
        return pressTimes;
    }

    public void setPressTimes(Integer pressTimes) {
        this.pressTimes = pressTimes;
    }

    public Long getAdminAuditId() {
        return adminAuditId;
    }

    public void setAdminAuditId(Long adminAuditId) {
        this.adminAuditId = adminAuditId;
    }

    public UserZhifubao getUserZhifubao() {
        return userZhifubao;
    }

    public void setUserZhifubao(UserZhifubao userZhifubao) {
        this.userZhifubao = userZhifubao;
    }

    public UserAuth getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(UserAuth userAuth) {
        this.userAuth = userAuth;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public boolean isOnceOverdueSign() {
        return onceOverdueSign;
    }

    public void setOnceOverdueSign(boolean onceOverdueSign) {
        this.onceOverdueSign = onceOverdueSign;
    }
}
