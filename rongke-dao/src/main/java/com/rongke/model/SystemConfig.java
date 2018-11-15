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

import java.io.Serializable;
import java.util.Date;

/**
 * @SystemConfig
 * @系统设置 (sys_config)
 * @version : Ver 1.0
 */
@TableName("sys_config")
public class SystemConfig implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * @备注:
     * @字段:id BIGINT(19)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * @备注
     * @字段: config_key VARCHAR(255)
     */
    private String configKey;

    /**
     * @备注
     * @字段: config_value VARCHAR(255)
     */
    private String configValue;

    /**
     * @备注:
     * @字段:create_time DATETIME(19)
     */
    @JsonSerialize(using=DateJsonSerializer.class)
    @JsonDeserialize(using=DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    private java.util.Date createTime = new java.util.Date();


    /**
     * @备注:
     * @字段:revise_time DATETIME(19)
     */
    @JsonSerialize(using=DateJsonSerializer.class)
    @JsonDeserialize(using=DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    private java.util.Date reviseTime = new java.util.Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReviseTime() {
        return reviseTime;
    }

    public void setReviseTime(Date reviseTime) {
        this.reviseTime = reviseTime;
    }
}
