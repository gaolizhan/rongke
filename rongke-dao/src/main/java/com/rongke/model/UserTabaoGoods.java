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
 * @UserTabaoGoods
 * @(user_tabao_goods)
 * @version : Ver 1.0
 */
@TableName("user_tabao_goods")
public class UserTabaoGoods implements Serializable {
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
	 * @备注:下单时间
	 * @字段:order_time VARCHAR(255)
	 */
	private String orderTime;


	/**
	 * @备注:收货地址
	 * @字段:receiver_addr VARCHAR(255)
	 */
	private String receiverAddr;


	/**
	 * @备注:收货人姓名
	 * @字段:receiver_name VARCHAR(255)
	 */
	private String receiverName;


	/**
	 * @备注:订单金额
	 * @字段:order_amount VARCHAR(255)
	 */
	private String orderAmount;


	/**
	 * @备注:订单状态
	 * @字段:order_status VARCHAR(255)
	 */
	private String orderStatus;


	/**
	 * @备注:商品名称
	 * @字段:product_name VARCHAR(255)
	 */
	private String productName;


	/**
	 * @备注:商铺
	 * @字段:order_shop VARCHAR(255)
	 */
	private String orderShop;


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


	public UserTabaoGoods(){
	}

	public UserTabaoGoods(
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


	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderTime() {
		return this.orderTime;
	}


	public void setReceiverAddr(String receiverAddr) {
		this.receiverAddr = receiverAddr;
	}

	public String getReceiverAddr() {
		return this.receiverAddr;
	}


	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverName() {
		return this.receiverName;
	}


	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderAmount() {
		return this.orderAmount;
	}


	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatus() {
		return this.orderStatus;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return this.productName;
	}


	public void setOrderShop(String orderShop) {
		this.orderShop = orderShop;
	}

	public String getOrderShop() {
		return this.orderShop;
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
