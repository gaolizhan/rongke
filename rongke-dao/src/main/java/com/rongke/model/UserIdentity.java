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
 * @UserIdentity
 * @(user_identity)
 */
@TableName("user_identity")
public class UserIdentity implements Serializable {
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
     * @备注:身份证正面
     * @字段:identity_front VARCHAR(255)
     */
    private java.lang.String identityFront;


    /**
     * @备注:反面
     * @字段:identity_back VARCHAR(255)
     */
    private java.lang.String identityBack;


    /**
     * @备注:人脸照片
     * @字段:face_url VARCHAR(255)
     */
    private java.lang.String faceUrl;

    /**
     * @备注:姓名
     * @字段:user_name VARCHAR(255)
     */
    private java.lang.String userName;

    /**
     * @备注:身份证号
     * @字段:identity_num VARCHAR(255)
     */
    private java.lang.String identityNum;

    /**
     * @备注:
     * @字段:qq_num VARCHAR(255)
     */
    private java.lang.String qqNum;

    /**
     * @备注:
     * @字段:address VARCHAR(255)
     */
    private java.lang.String address;

    /**
     * @备注:
     * @字段:address_details VARCHAR(255)
     */
    private java.lang.String addressDetails;

    /**
     * @备注:状态
     * @字段:status INT(33)
     */
    private java.lang.Integer status;


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


    public UserIdentity() {
    }

    public UserIdentity(
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


    public void setIdentityFront(java.lang.String identityFront) {
        this.identityFront = identityFront;
    }

    public java.lang.String getIdentityFront() {
        return this.identityFront;
    }


    public void setIdentityBack(java.lang.String identityBack) {
        this.identityBack = identityBack;
    }

    public java.lang.String getIdentityBack() {
        return this.identityBack;
    }


    public void setFaceUrl(java.lang.String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public java.lang.String getFaceUrl() {
        return this.faceUrl;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentityNum() {
        return identityNum;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }
}
