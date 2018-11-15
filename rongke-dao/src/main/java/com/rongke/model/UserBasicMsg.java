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
 * @UserBasicMsg
 * @(user_basic_msg)
 */
@TableName("user_basic_msg")
public class UserBasicMsg implements Serializable {
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
     * @备注:婚姻状况
     * @字段:marry VARCHAR(255)
     */
    private java.lang.String marry;


    /**
     * @备注:学历
     * @字段:study VARCHAR(255)
     */
    private java.lang.String study;


    /**
     * @备注:省
     * @字段:province VARCHAR(255)
     */
    private java.lang.String province;


    /**
     * @备注:市
     * @字段:city VARCHAR(255)
     */
    private java.lang.String city;


    /**
     * @备注:区
     * @字段:county VARCHAR(255)
     */
    private java.lang.String county;


    /**
     * @备注:区域代码
     * @字段:area_code VARCHAR(255)
     */
    private java.lang.String areaCode;


    /**
     * @备注:详细地址
     * @字段:address_details VARCHAR(255)
     */
    private java.lang.String addressDetails;


    /**
     * @备注:联系人一姓名
     * @字段:link_person_name_one VARCHAR(255)
     */
    private java.lang.String linkPersonNameOne;


    /**
     * @备注:联系人一电话
     * @字段:link_person_phone_one VARCHAR(255)
     */
    private java.lang.String linkPersonPhoneOne;


    /**
     * @备注:联系人一关系
     * @字段:link_person_relation_one VARCHAR(255)
     */
    private java.lang.String linkPersonRelationOne;


    /**
     * @备注:
     * @字段:link_person_name_two VARCHAR(255)
     */
    private java.lang.String linkPersonNameTwo;


    /**
     * @备注:
     * @字段:link_person_phone_two VARCHAR(255)
     */
    private java.lang.String linkPersonPhoneTwo;


    /**
     * @备注:
     * @字段:link_person_relation_two VARCHAR(255)
     */
    private java.lang.String linkPersonRelationTwo;

    /**
     * @备注:
     * @字段:work_company VARCHAR(255)
     */
    private java.lang.String workCompany;

    /**
     * @备注:
     * @字段:work_money VARCHAR(255)
     */
    private java.lang.String workMoney;

    /**
     * @备注:
     * @字段:work_phone VARCHAR(255)
     */
    private java.lang.String workPhone;

    /**
     * @备注:
     * @字段:work_place VARCHAR(255)
     */
    private java.lang.String workPlace;

    /**
     * @备注:
     * @字段:status integer(11)
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


    public UserBasicMsg() {
    }

    public UserBasicMsg(
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


    public void setMarry(java.lang.String marry) {
        this.marry = marry;
    }

    public java.lang.String getMarry() {
        return this.marry;
    }


    public void setStudy(java.lang.String study) {
        this.study = study;
    }

    public java.lang.String getStudy() {
        return this.study;
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


    public void setAddressDetails(java.lang.String addressDetails) {
        this.addressDetails = addressDetails;
    }

    public java.lang.String getAddressDetails() {
        return this.addressDetails;
    }


    public void setLinkPersonNameOne(java.lang.String linkPersonNameOne) {
        this.linkPersonNameOne = linkPersonNameOne;
    }

    public java.lang.String getLinkPersonNameOne() {
        return this.linkPersonNameOne;
    }


    public void setLinkPersonPhoneOne(java.lang.String linkPersonPhoneOne) {
        this.linkPersonPhoneOne = linkPersonPhoneOne;
    }

    public java.lang.String getLinkPersonPhoneOne() {
        return this.linkPersonPhoneOne;
    }


    public void setLinkPersonRelationOne(java.lang.String linkPersonRelationOne) {
        this.linkPersonRelationOne = linkPersonRelationOne;
    }

    public java.lang.String getLinkPersonRelationOne() {
        return this.linkPersonRelationOne;
    }


    public void setLinkPersonNameTwo(java.lang.String linkPersonNameTwo) {
        this.linkPersonNameTwo = linkPersonNameTwo;
    }

    public java.lang.String getLinkPersonNameTwo() {
        return this.linkPersonNameTwo;
    }


    public void setLinkPersonPhoneTwo(java.lang.String linkPersonPhoneTwo) {
        this.linkPersonPhoneTwo = linkPersonPhoneTwo;
    }

    public java.lang.String getLinkPersonPhoneTwo() {
        return this.linkPersonPhoneTwo;
    }


    public void setLinkPersonRelationTwo(java.lang.String linkPersonRelationTwo) {
        this.linkPersonRelationTwo = linkPersonRelationTwo;
    }

    public java.lang.String getLinkPersonRelationTwo() {
        return this.linkPersonRelationTwo;
    }


    public void setGmtDatetime(java.util.Date gmtDatetime) {
        this.gmtDatetime = gmtDatetime;
    }

    public java.util.Date getGmtDatetime() {
        return this.gmtDatetime;
    }


    public Date getUptDatetime() {
        return uptDatetime;
    }

    public void setUptDatetime(Date uptDatetime) {
        this.uptDatetime = uptDatetime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getWorkCompany() {
        return workCompany;
    }

    public void setWorkCompany(String workCompany) {
        this.workCompany = workCompany;
    }

    public String getWorkMoney() {
        return workMoney;
    }

    public void setWorkMoney(String workMoney) {
        this.workMoney = workMoney;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }
}
