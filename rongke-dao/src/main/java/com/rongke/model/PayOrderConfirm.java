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
 * @PayOrderConfirm
 * @(pay_order_confirm)
 * @version : Ver 1.0
 */
@TableName("pay_order_confirm")
public class PayOrderConfirm implements Serializable {
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
	 * @字段:no_order VARCHAR(255)
	 */
	private String noOrder;


	/**
	 * @备注:
	 * @字段:recive_name VARCHAR(255)
	 */
	private String reciveName;

	/**
	 * @备注:
	 * @字段:recive_bank_num VARCHAR(255)
	 */
	private String reciveBankNum;

	/**
	 * @备注:
	 * @字段:money VARCHAR(255)
	 */
	private String money;

	/**
	 * @备注:
	 * @字段:code VARCHAR(255)
	 */
	private String code;

	/**
	 * @备注:
	 * @字段:status INT(255)
	 */
	private Integer status;

	/**
	 * @备注:
	 * @字段:loan_order_id BIGINT(255)
	 */
	private Long loanOrderId;


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


	public PayOrderConfirm(){
	}

	public PayOrderConfirm(
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


	public void setNoOrder(String noOrder) {
		this.noOrder = noOrder;
	}

	public String getNoOrder() {
		return this.noOrder;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
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

	public String getReciveName() {
		return reciveName;
	}

	public void setReciveName(String reciveName) {
		this.reciveName = reciveName;
	}

	public String getReciveBankNum() {
		return reciveBankNum;
	}

	public void setReciveBankNum(String reciveBankNum) {
		this.reciveBankNum = reciveBankNum;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getLoanOrderId() {
		return loanOrderId;
	}

	public void setLoanOrderId(Long loanOrderId) {
		this.loanOrderId = loanOrderId;
	}
}
