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
 * @PressRecords
 * @(press_records)
 * @version : Ver 1.0
 */
@TableName("press_records")
public class PressRecords implements Serializable {
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
	 * @备注:催款人id
	 * @字段:press_money_man BIGINT(19)
	 */
	private Long pressMoneyMan;


	/**
	 * @备注:催收是否成功 1.成功 2失败
	 * @字段:is_success INT(10)
	 */
	private Integer isSuccess;


	/**
	 * @备注:催收备注
	 * @字段:content MEDIUMTEXT(16777215)
	 */
	private String content;


	/**
	 * @备注:订单id
	 * @字段:order_id BIGINT(19)
	 */
	private Long orderId;


	/**
	 * @备注:用户id
	 * @字段:user_id BIGINT(19)
	 */
	private Long userId;


	public PressRecords(){
	}

	public PressRecords(
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


	public void setPressMoneyMan(Long pressMoneyMan) {
		this.pressMoneyMan = pressMoneyMan;
	}

	public Long getPressMoneyMan() {
		return this.pressMoneyMan;
	}


	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Integer getIsSuccess() {
		return this.isSuccess;
	}


	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}


	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderId() {
		return this.orderId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return this.userId;
	}
}
