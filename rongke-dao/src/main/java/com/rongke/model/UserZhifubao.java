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
 * @UserZhifubao
 * @(user_zhifubao)
 * @version : Ver 1.0
 */
@TableName("user_zhifubao")
public class UserZhifubao implements Serializable {
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

	private String aliNumber;

	private String aliPwd;


	@TableField(exist = false)
	private User user;




	/**
	 * @备注:手机
	 * @字段:user_mobile VARCHAR(255)
	 */
	private String userMobile;


	/**
	 * @备注:身份证号
	 * @字段:identity_code VARCHAR(255)
	 */
	private String identityCode;


	/**
	 * @备注:用户名
	 * @字段:user_name VARCHAR(255)
	 */
	private String userName;



	@TableField(exist = false)
	private UserAuth userAuth;


	/**
	 * @备注:真实姓名
	 * @字段:real_name VARCHAR(255)
	 */
	private String realName;


	/**
	 * @备注:是否实名认证0未1已
	 * @字段:verified VARCHAR(255)
	 */
	private String verified;


	/**
	 * @备注:邮箱
	 * @字段:email VARCHAR(255)
	 */
	private String email;


	/**
	 * @备注:余额
	 * @字段:assets_yu_e VARCHAR(255)
	 */
	private String assetsYuE;


	/**
	 * @备注:余额宝
	 * @字段:assets_yu_e_bao VARCHAR(255)
	 */
	private String assetsYuEbao;


	/**
	 * @备注:花呗额度
	 * @字段:huabei_quota VARCHAR(255)
	 */
	private String huabeiQuota;


	/**
	 * @备注:花呗余额
	 * @字段:huabei_balance VARCHAR(255)
	 */
	private String huabeiBalance;


	/**
	 * @备注:花呗下一期还款金额
	 * @字段:huabei_next_repayment_amount VARCHAR(255)
	 */
	private String huabeiNextRepaymentAmount;



	private String jiebeiQuota;
	private String zhimaPoint;




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


	public UserZhifubao(){
	}

	public UserZhifubao(
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


	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserMobile() {
		return this.userMobile;
	}


	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}

	public String getIdentityCode() {
		return this.identityCode;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return this.userName;
	}

	public String getAliNumber() {
		return aliNumber;
	}

	public void setAliNumber(String aliNumber) {
		this.aliNumber = aliNumber;
	}

	public String getAliPwd() {
		return aliPwd;
	}

	public void setAliPwd(String aliPwd) {
		this.aliPwd = aliPwd;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRealName() {
		return this.realName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getVerified() {
		return this.verified;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}


	public void setAssetsYuE(String assetsYuE) {
		this.assetsYuE = assetsYuE;
	}

	public String getAssetsYuE() {
		return this.assetsYuE;
	}


	public void setAssetsYuEbao(String assetsYuEbao) {
		this.assetsYuEbao = assetsYuEbao;
	}

	public String getAssetsYuEbao() {
		return this.assetsYuEbao;
	}


	public void setHuabeiQuota(String huabeiQuota) {
		this.huabeiQuota = huabeiQuota;
	}

	public String getHuabeiQuota() {
		return this.huabeiQuota;
	}


	public void setHuabeiBalance(String huabeiBalance) {
		this.huabeiBalance = huabeiBalance;
	}

	public String getHuabeiBalance() {
		return this.huabeiBalance;
	}


	public void setHuabeiNextRepaymentAmount(String huabeiNextRepaymentAmount) {
		this.huabeiNextRepaymentAmount = huabeiNextRepaymentAmount;
	}

	public String getHuabeiNextRepaymentAmount() {
		return this.huabeiNextRepaymentAmount;
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

	public String getJiebeiQuota() {
		return jiebeiQuota;
	}

	public void setJiebeiQuota(String jiebeiQuota) {
		this.jiebeiQuota = jiebeiQuota;
	}

	public String getZhimaPoint() {
		return zhimaPoint;
	}

	public void setZhimaPoint(String zhimaPoint) {
		this.zhimaPoint = zhimaPoint;
	}

	public UserAuth getUserAuth() {
		return userAuth;
	}

	public void setUserAuth(UserAuth userAuth) {
		this.userAuth = userAuth;
	}
}
