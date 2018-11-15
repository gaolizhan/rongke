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
 * @UserBank
 * @(user_bank)
 * @version : Ver 1.0
 */
@TableName("user_bank")
public class UserBank implements Serializable {
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
	 * @备注:银行名称
	 * @字段:bank_name VARCHAR(255)
	 */
	private String bankName;


	/**
	 * @备注:卡名称
	 * @字段:cardname VARCHAR(255)
	 */
	private String cardname;


	/**
	 * @备注:卡类型
	 * @字段:cardtype VARCHAR(255)
	 */
	private String cardtype;


	/**
	 * @备注:银行卡号码
	 * @字段:bankcardno VARCHAR(255)
	 */
	private String bankcardno;


	/**
	 * @备注:身份证号
	 * @字段:idcardno VARCHAR(255)
	 */
	private String idcardno;


	/**
	 * @备注:用户生日
	 * @字段:birthday VARCHAR(255)
	 */
	private String birthday;


	/**
	 * @备注:身份证地址
	 * @字段:address VARCHAR(255)
	 */
	private String address;


	/**
	 * @备注:预留手机号
	 * @字段:bank_phone VARCHAR(255)
	 */
	private String bankPhone;


	/**
	 * @备注:姓名
	 * @字段:name VARCHAR(255)
	 */
	private String name;



	/**
	 * @备注:号码归属城市
	 * @字段:mobile_city VARCHAR(255)
	 */
	private String mobileCity;

	/**
	 * @备注:绑定id
	 * @字段:mobile_city VARCHAR(255)
	 */
	private String bindId;


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


	public UserBank(){
	}

	public UserBank(
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


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankName() {
		return this.bankName;
	}


	public void setCardname(String cardname) {
		this.cardname = cardname;
	}

	public String getCardname() {
		return this.cardname;
	}


	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getCardtype() {
		return this.cardtype;
	}


	public void setBankcardno(String bankcardno) {
		this.bankcardno = bankcardno;
	}

	public String getBankcardno() {
		return this.bankcardno;
	}


	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}

	public String getIdcardno() {
		return this.idcardno;
	}


	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthday() {
		return this.birthday;
	}


	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}


	public void setBankPhone(String bankPhone) {
		this.bankPhone = bankPhone;
	}

	public String getBankPhone() {
		return this.bankPhone;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}



	public void setMobileCity(String mobileCity) {
		this.mobileCity = mobileCity;
	}

	public String getMobileCity() {
		return this.mobileCity;
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

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}
}
