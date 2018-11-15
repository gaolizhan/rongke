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
 * @LoginRecord
 * @(login_record)
 * @version : Ver 1.0
 */
@TableName("login_record")
public class LoginRecord implements Serializable {
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
	 * @字段:user_name VARCHAR(55)
	 */
	private String userName;


	/**
	 * @备注:
	 * @字段:ip VARCHAR(55)
	 */
	private String ip;


	/**
	 * @备注:
	 * @字段:login_time DATETIME(19)
	 */
	private java.util.Date loginTime;


	/**
	 * @备注:
	 * @字段:phone VARCHAR(55)
	 */
	private String phone;


	/**
	 * @备注:
	 * @字段:app_version VARCHAR(55)
	 */
	private String appVersion;


	/**
	 * @备注:类型：1(后台登录) 2(app登录)
	 * @字段:type INT(10)
	 */
	private Integer type;


	public LoginRecord(){
	}

	public LoginRecord(
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


	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return this.userName;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return this.ip;
	}


	public void setLoginTime(java.util.Date loginTime) {
		this.loginTime = loginTime;
	}

	public java.util.Date getLoginTime() {
		return this.loginTime;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}


	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppVersion() {
		return this.appVersion;
	}


	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return this.type;
	}
}
