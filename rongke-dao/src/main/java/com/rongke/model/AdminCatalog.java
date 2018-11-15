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
 * @AdminCatalog
 * @(admin_catalog)
 */
@TableName("admin_catalog")
public class AdminCatalog implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * @备注:目录表id
     * @字段:id INT(10)
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;


    /**
     * @备注:目录名字
     * @字段:catalog_name VARCHAR(300)
     */
    private java.lang.String catalogName;


    /**
     * @备注:目录url
     * @字段:catalog_url VARCHAR(300)
     */
    private java.lang.String catalogUrl;


    /**
     * @备注:父目录，没有则为null
     * @字段:parent_id INT(10)
     */
    private java.lang.Integer parentId;


    /**
     * @备注:状态1代表使用2代表废弃
     * @字段:status INT(10)
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


    public AdminCatalog() {
    }

    public AdminCatalog(
            java.lang.Integer id
    ) {
        this.id = id;
    }


    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.Integer getId() {
        return this.id;
    }


    public void setCatalogName(java.lang.String catalogName) {
        this.catalogName = catalogName;
    }

    public java.lang.String getCatalogName() {
        return this.catalogName;
    }


    public void setCatalogUrl(java.lang.String catalogUrl) {
        this.catalogUrl = catalogUrl;
    }

    public java.lang.String getCatalogUrl() {
        return this.catalogUrl;
    }


    public void setParentId(java.lang.Integer parentId) {
        this.parentId = parentId;
    }

    public java.lang.Integer getParentId() {
        return this.parentId;
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
