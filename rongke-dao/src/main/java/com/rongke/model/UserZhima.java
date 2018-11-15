package com.rongke.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * @UserZhima
 * @芝麻信用(user_zhima)
 * @version : Ver 1.0
 */
@TableName("user_zhima")
public class UserZhima implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * @备注:用户id
	 * @字段:user_id INT(10)
	 */
	private Long userId;


	/**
	 * @备注:芝麻用户openid
	 * @字段:open_id varchar(50)
	 */
	private String openId;


	/**
	 * @备注:芝麻分
	 * @字段:score VARCHAR(255)
	 */
	private Integer score;


	/**
	 * @备注:
	 * @字段:addtime DATETIME(19)
	 */
	private java.util.Date addtime;


	/**
	 * @备注:
	 * @字段:updtime DATETIME(19)
	 */
	private java.util.Date updtime;

	/**
	 * 流水凭证
	 */
	private String transactionId;

	public UserZhima(){
	}

	public UserZhima(
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


	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getOpenId() {
		return this.openId;
	}


	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getScore() {
		return this.score;
	}


	public void setAddtime(java.util.Date addtime) {
		this.addtime = addtime;
	}

	public java.util.Date getAddtime() {
		return this.addtime;
	}


	public void setUpdtime(java.util.Date updtime) {
		this.updtime = updtime;
	}

	public java.util.Date getUpdtime() {
		return this.updtime;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
}
