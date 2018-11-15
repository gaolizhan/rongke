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
 * @AddressLog
 * @地址信息(address_log)
 */
@TableName("address_log")
public class AddressLog implements Serializable {
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
     * @备注:用户名
     * @字段:user_name VARCHAR(128)
     */
    private java.lang.String userName;


    /**
     * @备注:用户手机
     * @字段:user_phone VARCHAR(45)
     */
    private java.lang.String userPhone;


    /**
     * @备注:省
     * @字段:province VARCHAR(45)
     */
    private java.lang.String province;


    /**
     * @备注:市
     * @字段:city VARCHAR(45)
     */
    private java.lang.String city;


    /**
     * @备注:区
     * @字段:county VARCHAR(45)
     */
    private java.lang.String county;


    /**
     * @备注:省代码，若无则用市代码
     * @字段:area_code VARCHAR(45)
     */
    private java.lang.String areaCode;


    /**
     * @备注:区代码
     * @字段:county_code VARCHAR(45)
     */
    private java.lang.String countyCode;


    /**
     * @备注:详细地址
     * @字段:address VARCHAR(1024)
     */
    private java.lang.String address;


    public AddressLog() {
    }

    public AddressLog(
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


    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    public java.lang.String getUserName() {
        return this.userName;
    }


    public void setUserPhone(java.lang.String userPhone) {
        this.userPhone = userPhone;
    }

    public java.lang.String getUserPhone() {
        return this.userPhone;
    }


    public void setProvince(java.lang.String province) {
        this.province = province;
    }

    public java.lang.String getProvince() {
        return this.province;
    }


    public void setCity(java.lang.String city) {
        this.city = city;
    }

    public java.lang.String getCity() {
        return this.city;
    }


    public void setCounty(java.lang.String county) {
        this.county = county;
    }

    public java.lang.String getCounty() {
        return this.county;
    }


    public void setAreaCode(java.lang.String areaCode) {
        this.areaCode = areaCode;
    }

    public java.lang.String getAreaCode() {
        return this.areaCode;
    }


    public void setCountyCode(java.lang.String countyCode) {
        this.countyCode = countyCode;
    }

    public java.lang.String getCountyCode() {
        return this.countyCode;
    }


    public void setAddress(java.lang.String address) {
        this.address = address;
    }

    public java.lang.String getAddress() {
        return this.address;
    }
}
