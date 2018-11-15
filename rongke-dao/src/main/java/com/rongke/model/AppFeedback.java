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
 * @AppFeedback
 * @(app_feedback)
 */
@TableName("app_feedback")
public class AppFeedback implements Serializable {
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
     * @备注:反馈类型
     * @字段:type INT(10)
     */
    private java.lang.Integer type;


    /**
     * @备注:
     * @字段:content MEDIUMTEXT(16777215)
     */
    private java.lang.String content;


    /**
     * @备注:
     * @字段:img_url VARCHAR(255)
     */
    private java.lang.String imgUrl;


    /**
     * @备注:手机号
     * @字段:phone VARCHAR(255)
     */
    private java.lang.String phone;


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


    public AppFeedback() {
    }

    public AppFeedback(
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


    public void setType(java.lang.Integer type) {
        this.type = type;
    }

    public java.lang.Integer getType() {
        return this.type;
    }


    public void setContent(java.lang.String content) {
        this.content = content;
    }

    public java.lang.String getContent() {
        return this.content;
    }


    public void setImgUrl(java.lang.String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public java.lang.String getImgUrl() {
        return this.imgUrl;
    }


    public void setPhone(java.lang.String phone) {
        this.phone = phone;
    }

    public java.lang.String getPhone() {
        return this.phone;
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
