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
 * @AboutXed
 * @(about_xed)
 */
@TableName("about_xed")
public class AboutXed implements Serializable {
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
     * @字段:about_us MEDIUMTEXT(16777215)
     */
    private java.lang.String aboutUs;


    /**
     * @备注:
     * @字段:serve_phone VARCHAR(255)
     */
    private java.lang.String servePhone;


    /**
     * @备注:
     * @字段:wexin VARCHAR(255)
     */
    private java.lang.String wexin;


    public AboutXed() {
    }

    public AboutXed(
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


    public void setAboutUs(java.lang.String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public java.lang.String getAboutUs() {
        return this.aboutUs;
    }


    public void setServePhone(java.lang.String servePhone) {
        this.servePhone = servePhone;
    }

    public java.lang.String getServePhone() {
        return this.servePhone;
    }


    public void setWexin(java.lang.String wexin) {
        this.wexin = wexin;
    }

    public java.lang.String getWexin() {
        return this.wexin;
    }
}
