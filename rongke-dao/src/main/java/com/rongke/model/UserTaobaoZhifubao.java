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
 * @UserTaobaoZhifubao
 * @(user_taobao_zhifubao)
 * @version : Ver 1.0
 */
@TableName("user_taobao_zhifubao")
public class UserTaobaoZhifubao implements Serializable {
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
	 * @备注:花呗余额
	 * @字段:huabei_can_use_money VARCHAR(255)
	 */
	private String huabeiCanUseMoney;


	/**
	 * @备注:花呗额度
	 * @字段:huabei_total_amount VARCHAR(255)
	 */
	private String huabeiTotalAmount;


	/**
	 * @备注:支付宝余额
	 * @字段:alipay_remaining_amount VARCHAR(255)
	 */
	private String alipayRemainingAmount;


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


	public UserTaobaoZhifubao(){
	}

	public UserTaobaoZhifubao(
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


	public void setHuabeiCanUseMoney(String huabeiCanUseMoney) {
		this.huabeiCanUseMoney = huabeiCanUseMoney;
	}

	public String getHuabeiCanUseMoney() {
		return this.huabeiCanUseMoney;
	}


	public void setHuabeiTotalAmount(String huabeiTotalAmount) {
		this.huabeiTotalAmount = huabeiTotalAmount;
	}

	public String getHuabeiTotalAmount() {
		return this.huabeiTotalAmount;
	}


	public void setAlipayRemainingAmount(String alipayRemainingAmount) {
		this.alipayRemainingAmount = alipayRemainingAmount;
	}

	public String getAlipayRemainingAmount() {
		return this.alipayRemainingAmount;
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
}
