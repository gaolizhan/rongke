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
 * @AdminAuthority
 * @(admin_authority)
 */
@TableName("admin_authority")
public class AdminAuthority implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * @备注:权限表id
     * @字段:id BIGINT(19)
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;


    /**
     * @备注:管理员id
     * @字段:admin_id BIGINT(19)
     */
    private java.lang.Long adminId;


    /**
     * @备注:目录id
     * @字段:catalog_id BIGINT(19)
     */
    private java.lang.Long catalogId;


    /**
     * @备注:状态:默认1正常,2删除
     * @字段:status INT(10)
     */
    private java.lang.Integer status;


    /**
     * @备注:创建时间
     * @字段:gmt_datetime DATE(10)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date gmtDatetime = new java.util.Date();


    /**
     * @备注:更新时间
     * @字段:upt_datetime DATE(10)
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date uptDatetime = new java.util.Date();


    public AdminAuthority() {
    }

    public AdminAuthority(
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


    public void setAdminId(java.lang.Long adminId) {
        this.adminId = adminId;
    }

    public java.lang.Long getAdminId() {
        return this.adminId;
    }


    public void setCatalogId(java.lang.Long catalogId) {
        this.catalogId = catalogId;
    }

    public java.lang.Long getCatalogId() {
        return this.catalogId;
    }


    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    public java.lang.Integer getStatus() {
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
}
