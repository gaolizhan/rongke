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
 * @TbReceiver
 * @淘宝收货地址(tb_receiver)
 * @version : Ver 1.0
 */
@TableName("tb_receiver")
public class TbReceiver implements Serializable {
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
	 * @备注:收货人
	 * @字段:name VARCHAR(255)
	 */
	private String name;


	/**
	 * @备注:地区
	 * @字段:area VARCHAR(255)
	 */
	private String area;


	/**
	 * @备注:地址
	 * @字段:address VARCHAR(255)
	 */
	private String address;


	/**
	 * @备注:手机号
	 * @字段:mobile VARCHAR(255)
	 */
	private String mobile;


	/**
	 * @备注:固定电话
	 * @字段:telephone VARCHAR(255)
	 */
	private String telephone;


	/**
	 * @备注:默认地址 1默认 0非默认
	 * @字段:default_area VARCHAR(255)
	 */
	private String defaultArea;


	/**
	 * @备注:邮编
	 * @字段:zip_code VARCHAR(255)
	 */
	private String zipCode;


	/**
	 * @备注:
	 * @字段:gmt_datetime DATETIME(19)
	 */
	@JsonSerialize(using=DateJsonSerializer.class)
	@JsonDeserialize(using=DateJsonDeserializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	private java.util.Date gmtDatetime = new java.util.Date();


	public TbReceiver(){
	}

	public TbReceiver(
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


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}


	public void setArea(String area) {
		this.area = area;
	}

	public String getArea() {
		return this.area;
	}


	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return this.mobile;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTelephone() {
		return this.telephone;
	}


	public void setDefaultArea(String defaultArea) {
		this.defaultArea = defaultArea;
	}

	public String getDefaultArea() {
		return this.defaultArea;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getZipCode() {
		return this.zipCode;
	}


	public void setGmtDatetime(java.util.Date gmtDatetime) {
		this.gmtDatetime = gmtDatetime;
	}

	public java.util.Date getGmtDatetime() {
		return this.gmtDatetime;
	}
}
