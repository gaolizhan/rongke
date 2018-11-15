package com.rongke.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * @UserLoginLog
 * @(user_login_log)
 * @version : Ver 1.0
 */
@TableName("user_login_log")
public class UserLoginLog implements Serializable {
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
	 * @备注:登录时间
	 * @字段:login_time VARCHAR(255)
	 */
	private String loginTime;


	/**
	 * @备注:经度
	 * @字段:lng VARCHAR(255)
	 */
	private String lng;


	private transient String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	private transient  String appVersion;


	/**
	 * @备注:纬度
	 * @字段:lat VARCHAR(255)
	 */
	private String lat;


	/**
	 * @备注:
	 * @字段:address_details VARCHAR(255)
	 */
	private String addressDetails;


	public UserLoginLog(){
	}

	public UserLoginLog(
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


	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLng() {
		return this.lng;
	}


	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLat() {
		return this.lat;
	}


	public void setAddressDetails(String addressDetails) {
		this.addressDetails = addressDetails;
	}

	public String getAddressDetails() {
		return this.addressDetails;
	}
}
