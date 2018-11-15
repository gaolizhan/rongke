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
 * @UserTongdunfen
 * @(user_tongdunfen)
 * @version : Ver 1.0
 */
@TableName("user_tongdunfen")
public class UserTongdunfen implements Serializable {
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
	 * @备注:风险名称
	 * @字段:risk_name VARCHAR(1000)
	 */
	private String riskName;


	/**
	 * @备注:
	 * @字段:rule_id VARCHAR(255)
	 */
	private String ruleId;


	/**
	 * @备注:风险分
	 * @字段:score VARCHAR(25)
	 */
	private String score;

	/**
	 * @备注:
	 * @字段:order_number VARCHAR(255)
	 */
	private String orderNumber;


	/**
	 * @备注:
	 * @字段:gmt_datetime DATETIME(19)
	 */
	@JsonSerialize(using=DateJsonSerializer.class)
	@JsonDeserialize(using=DateJsonDeserializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	private java.util.Date gmtDatetime = new java.util.Date();


	public UserTongdunfen(){
	}

	public UserTongdunfen(
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


	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public String getRiskName() {
		return this.riskName;
	}


	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleId() {
		return this.ruleId;
	}


	public void setScore(String score) {
		this.score = score;
	}

	public String getScore() {
		return this.score;
	}


	public void setGmtDatetime(java.util.Date gmtDatetime) {
		this.gmtDatetime = gmtDatetime;
	}

	public java.util.Date getGmtDatetime() {
		return this.gmtDatetime;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
}
