package com.rongke.model;

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

@TableName("app_version_info")
public class AppVersionInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using=DateJsonSerializer.class)
    @JsonDeserialize(using=DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date createTime;

    private String versionNum;

    private String iosUrl;

    private String androidUrl;

    private Boolean forcedUpdating;

    private String source;

    private String updateContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl == null ? null : iosUrl.trim();
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl == null ? null : androidUrl.trim();
    }

    public Boolean getForcedUpdating() {
        return forcedUpdating;
    }

    public void setForcedUpdating(Boolean forcedUpdating) {
        this.forcedUpdating = forcedUpdating;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent == null ? null : updateContent.trim();
    }
}