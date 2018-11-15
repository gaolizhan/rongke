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
 * @UserPhoneList
 * @(user_phone_list)
 * @version : Ver 1.0
 */
@TableName("user_phone_list")
public class UserPhoneList implements Serializable {
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
	 * @字段:user_id BIGINT(19)
	 */
	private Long userId;
	/**
	 * 备注:
	 */
	@TableField(exist = false)
	private User user;


	/**
	 * @备注:联系人姓名
	 * @字段:name VARCHAR(255)
	 */
	private String name;


	/**
	 * @备注:联系电话
	 * @字段:phone VARCHAR(255)
	 */
	private String phone;


	/**
	 * @备注:关系
	 * @字段:link VARCHAR(255)
	 */
	private String link;

	private String callCountActive;
	private String callCountPassive;



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



	private String belongArea;
	private  Integer callTimes;
	private String callTime;



	public UserPhoneList(){
	}

	public UserPhoneList(
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
	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}


	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return this.link;
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

	public String getBelongArea() {
		return belongArea;
	}

	public void setBelongArea(String belongArea) {
		this.belongArea = belongArea;
	}

	public Integer getCallTimes() {
		return callTimes;
	}

	public void setCallTimes(Integer callTimes) {
		this.callTimes = callTimes;
	}

	public String getCallTime() {
		return callTime;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public String getCallCountActive() {
		return callCountActive;
	}

	public void setCallCountActive(String callCountActive) {
		this.callCountActive = callCountActive;
	}

	public String getCallCountPassive() {
		return callCountPassive;
	}

	public void setCallCountPassive(String callCountPassive) {
		this.callCountPassive = callCountPassive;
	}
}
