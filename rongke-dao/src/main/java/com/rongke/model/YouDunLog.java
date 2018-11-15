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
 * @YouDunLog
 * @(you_dun_log)
 * @version : Ver 1.0
 */
@TableName("you_dun_log")
public class YouDunLog implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:主键id
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * @备注:用户id
	 * @字段:user_id BIGINT(19)
	 */
	private Long userId;


	/**
	 * @备注:有盾实名认证商户唯一订单号
	 * @字段:identity_order_no VARCHAR(255)
	 */
	private String identityOrderNo;


	/**
	 * @备注:创建时间
	 * @字段:created_time DATETIME(19)
	 */
	private java.util.Date createdTime;


	public YouDunLog(){
	}

	public YouDunLog(
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


	public void setIdentityOrderNo(String identityOrderNo) {
		this.identityOrderNo = identityOrderNo;
	}

	public String getIdentityOrderNo() {
		return this.identityOrderNo;
	}


	public void setCreatedTime(java.util.Date createdTime) {
		this.createdTime = createdTime;
	}

	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}
}
