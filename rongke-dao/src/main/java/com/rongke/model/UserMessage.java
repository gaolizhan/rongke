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
 * @UserMessage
 * @用户推送消息(user_message)
 * @version : Ver 1.0
 */
@TableName("user_message")
public class UserMessage implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:主键id
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * @备注:用户id
	 * @字段:user_id BIGINT(19)
	 */
	private Long userId;


	/**
	 * @备注:标题
	 * @字段:title VARCHAR(55)
	 */
	private String title;


	/**
	 * @备注:内容
	 * @字段:text TEXT(65535)
	 */
	private String text;


	/**
	 * @备注:创建时间
	 * @字段:gmt_datetime DATETIME(19)
	 */
	@JsonSerialize(using=DateJsonSerializer.class)
	@JsonDeserialize(using=DateJsonDeserializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	private java.util.Date gmtDatetime = new java.util.Date();


	/**
	 * @备注:状态：1,正常 2,删除
	 * @字段:status INT(10)
	 */
	private Integer status;


	public UserMessage(){
	}

	public UserMessage(
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


	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return this.userId;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}


	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}


	public void setGmtDatetime(java.util.Date gmtDatetime) {
		this.gmtDatetime = gmtDatetime;
	}

	public java.util.Date getGmtDatetime() {
		return this.gmtDatetime;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}
}
