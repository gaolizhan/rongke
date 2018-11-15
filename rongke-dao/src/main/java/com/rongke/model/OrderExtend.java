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
 * @OrderExtend
 * @(order_extend)
 * @version : Ver 1.0
 */
@TableName("order_extend")
public class OrderExtend implements Serializable {
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
	 * @字段:order_id BIGINT(19)
	 */
	private Long orderId;
	/**
	 * 备注:
	 */
	@TableField(exist = false)
	private LoanOrder order;


	/**
	 * @备注:续期天数
	 * @字段:extend_days INT(10)
	 */
	private Integer extendDays;


	/**
	 * @备注:
	 * @字段:extend_money DECIMAL(33)
	 */
	private java.math.BigDecimal extendMoney;


	/**
	 * @备注:续期连连支付单号
	 * @字段:extend_lianlian_num VARCHAR(255)
	 */
	private String extendLianlianNum;


	/**
	 * @备注:连连还款计划单号
	 * @字段:repayment_no VARCHAR(255)
	 */
	private String repaymentNo;


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


	/**
	 * @备注:0续期未成功 1续期成功
	 * @字段:status INT(10)
	 */
	private Integer status;

	@TableField(exist = false)
	private String  userName;
	@TableField(exist = false)
	private String phone;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public OrderExtend(){
	}

	public OrderExtend(
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

	public void setOrderId(Long orderId) {
			this.orderId = orderId;
			}

	public Long getOrderId() {
			return this.orderId;
	}
	public void setOrder(LoanOrder order) {
		this.order = order;
	}

	public LoanOrder getOrder() {
		return this.order;
	}


	public void setExtendDays(Integer extendDays) {
		this.extendDays = extendDays;
	}

	public Integer getExtendDays() {
		return this.extendDays;
	}


	public void setExtendMoney(java.math.BigDecimal extendMoney) {
		this.extendMoney = extendMoney;
	}

	public java.math.BigDecimal getExtendMoney() {
		return this.extendMoney;
	}


	public void setExtendLianlianNum(String extendLianlianNum) {
		this.extendLianlianNum = extendLianlianNum;
	}

	public String getExtendLianlianNum() {
		return this.extendLianlianNum;
	}


	public void setRepaymentNo(String repaymentNo) {
		this.repaymentNo = repaymentNo;
	}

	public String getRepaymentNo() {
		return this.repaymentNo;
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


	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}
}
