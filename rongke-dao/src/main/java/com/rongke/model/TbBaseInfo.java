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
 * @TbBaseInfo
 * @个人信息(tb_base_info)
 * @version : Ver 1.0
 */
@TableName("tb_base_info")
public class TbBaseInfo implements Serializable {
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
	 * @备注:用户名
	 * @字段:user_name VARCHAR(255)
	 */
	private String userName;


	/**
	 * @备注:邮箱
	 * @字段:email VARCHAR(255)
	 */
	private String email;


	/**
	 * @备注:用户等级
	 * @字段:user_level VARCHAR(255)
	 */
	private String userLevel;


	/**
	 * @备注:昵称
	 * @字段:nick_name VARCHAR(255)
	 */
	private String nickName;


	/**
	 * @备注:真实姓名
	 * @字段:name VARCHAR(255)
	 */
	private String name;


	/**
	 * @备注:性别
	 * @字段:gender VARCHAR(255)
	 */
	private String gender;


	/**
	 * @备注:绑定手机号
	 * @字段:mobile VARCHAR(255)
	 */
	private String mobile;


	/**
	 * @备注:实名认证姓名
	 * @字段:real_name VARCHAR(255)
	 */
	private String realName;


	/**
	 * @备注:实名认证身份证
	 * @字段:identity_code VARCHAR(255)
	 */
	private String identityCode;


	/**
	 * @备注:会员成长值
	 * @字段:vip_count VARCHAR(255)
	 */
	private String vipCount;


	/**
	 * @备注:
	 * @字段:gmt_datetime DATETIME(19)
	 */
	@JsonSerialize(using=DateJsonSerializer.class)
	@JsonDeserialize(using=DateJsonDeserializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	private java.util.Date gmtDatetime = new java.util.Date();


	public TbBaseInfo(){
	}

	public TbBaseInfo(
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


	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return this.userName;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}


	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getUserLevel() {
		return this.userLevel;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return this.nickName;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return this.gender;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return this.mobile;
	}


	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRealName() {
		return this.realName;
	}


	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}

	public String getIdentityCode() {
		return this.identityCode;
	}


	public void setVipCount(String vipCount) {
		this.vipCount = vipCount;
	}

	public String getVipCount() {
		return this.vipCount;
	}


	public void setGmtDatetime(java.util.Date gmtDatetime) {
		this.gmtDatetime = gmtDatetime;
	}

	public java.util.Date getGmtDatetime() {
		return this.gmtDatetime;
	}
}
