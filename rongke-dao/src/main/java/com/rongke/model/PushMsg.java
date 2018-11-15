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
 * @PushMsg
 * @(push_msg)
 * @version : Ver 1.0
 */
@TableName("push_msg")
public class PushMsg implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * @备注:
	 * @字段:title VARCHAR(255)
	 */
	private String title;


	/**
	 * @备注:
	 * @字段:content VARCHAR(2550)
	 */
	private String content;


	/**
	 * @备注:
	 * @字段:img_url VARCHAR(255)
	 */
	private String imgUrl;


	/**
	 * @备注:
	 * @字段:link_url VARCHAR(255)
	 */
	private String linkUrl;


	/**
	 * @备注:
	 * @字段:type VARCHAR(255)
	 */
	private String type;


	/**
	 * @备注:
	 * @字段:gmt_datetime DATETIME(19)
	 */
	@JsonSerialize(using=DateJsonSerializer.class)
	@JsonDeserialize(using=DateJsonDeserializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	private java.util.Date gmtDatetime = new java.util.Date();


	/**
	 * @备注:
	 * @字段:upt_datetime DATETIME(19)
	 */
	@JsonSerialize(using=DateJsonSerializer.class)
	@JsonDeserialize(using=DateJsonDeserializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	private java.util.Date uptDatetime = new java.util.Date();


	/**
	 * @备注:0微推送1已推送
	 * @字段:status INT(10)
	 */
	private Integer status;


	public PushMsg(){
	}

	public PushMsg(
		Long id
	){
		this.id = id;
	}



	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}


	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}


	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgUrl() {
		return this.imgUrl;
	}


	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkUrl() {
		return this.linkUrl;
	}


	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
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


	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}
}
