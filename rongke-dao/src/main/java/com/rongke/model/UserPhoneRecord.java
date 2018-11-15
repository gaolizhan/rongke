package com.rongke.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * @UserPhoneRecord
 * @用户通话记录(user_phone_record)
 * @version : Ver 1.0
 */
@TableName("user_phone_record")
public class UserPhoneRecord implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * @备注:号码归属地
	 * @字段:belong_area VARCHAR(255)
	 */
	private String belongArea;


	/**
	 * @备注:
	 * @字段:called_times VARCHAR(255)
	 */
	private String calledTimes;


	/**
	 * @备注:
	 * @字段:conn_times VARCHAR(255)
	 */
	private String connTimes;


	/**
	 * @备注:
	 * @字段:call_times VARCHAR(255)
	 */
	private String callTimes;


	/**
	 * @备注:
	 * @字段:identify_info VARCHAR(255)
	 */
	private String identifyInfo;


	/**
	 * @备注:
	 * @字段:phone_no VARCHAR(255)
	 */
	private String phoneNo;


	/**
	 * @备注:联通号码
	 * @字段:anotherNm VARCHAR(255)
	 */
	private String anotherNm;


	/**
	 * @备注:费用
	 * @字段:commFee VARCHAR(255)
	 */
	private String commFee;


	/**
	 * @备注:主被叫
	 * @字段:commMode VARCHAR(255)
	 */
	private String commMode;


	/**
	 * @备注:号码归属地
	 * @字段:commPlac VARCHAR(255)
	 */
	private String commPlac;


	/**
	 * @备注:通话时间
	 * @字段:commTime VARCHAR(255)
	 */
	private String commTime;


	/**
	 * @备注:开始时间
	 * @字段:startTime VARCHAR(255)
	 */
	private String startTime;


	/**
	 * @备注:电信电话号码
	 * @字段:commPhoneNo VARCHAR(255)
	 */
	private String commPhoneNo;


	/**
	 * @备注:通话时间
	 * @字段:commTotalTime VARCHAR(255)
	 */
	private String commTotalTime;


	/**
	 * @备注:通话时期
	 * @字段:commDate VARCHAR(255)
	 */
	private String commDate;


	/**
	 * @备注:归属地
	 * @字段:commAreaCode VARCHAR(255)
	 */
	private String commAreaCode;


	/**
	 * @备注:主被叫
	 * @字段:callType VARCHAR(255)
	 */
	private String callType;


	/**
	 * @备注:1移动2联通3电信
	 * @字段:type INT(10)
	 */
	private Integer type;


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


	public UserPhoneRecord(){
	}

	public UserPhoneRecord(
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


	public void setBelongArea(String belongArea) {
		this.belongArea = belongArea;
	}

	public String getBelongArea() {
		return this.belongArea;
	}


	public void setCalledTimes(String calledTimes) {
		this.calledTimes = calledTimes;
	}

	public String getCalledTimes() {
		return this.calledTimes;
	}


	public void setConnTimes(String connTimes) {
		this.connTimes = connTimes;
	}

	public String getConnTimes() {
		return this.connTimes;
	}


	public void setCallTimes(String callTimes) {
		this.callTimes = callTimes;
	}

	public String getCallTimes() {
		return this.callTimes;
	}


	public void setIdentifyInfo(String identifyInfo) {
		this.identifyInfo = identifyInfo;
	}

	public String getIdentifyInfo() {
		return this.identifyInfo;
	}


	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPhoneNo() {
		return this.phoneNo;
	}


	public void setAnotherNm(String anotherNm) {
		this.anotherNm = anotherNm;
	}

	public String getAnotherNm() {
		return this.anotherNm;
	}


	public void setCommFee(String commFee) {
		this.commFee = commFee;
	}

	public String getCommFee() {
		return this.commFee;
	}


	public void setCommMode(String commMode) {
		this.commMode = commMode;
	}

	public String getCommMode() {
		return this.commMode;
	}


	public void setCommPlac(String commPlac) {
		this.commPlac = commPlac;
	}

	public String getCommPlac() {
		return this.commPlac;
	}


	public void setCommTime(String commTime) {
		this.commTime = commTime;
	}

	public String getCommTime() {
		return this.commTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartTime() {
		return this.startTime;
	}


	public void setCommPhoneNo(String commPhoneNo) {
		this.commPhoneNo = commPhoneNo;
	}

	public String getCommPhoneNo() {
		return this.commPhoneNo;
	}


	public void setCommTotalTime(String commTotalTime) {
		this.commTotalTime = commTotalTime;
	}

	public String getCommTotalTime() {
		return this.commTotalTime;
	}


	public void setCommDate(String commDate) {
		this.commDate = commDate;
	}

	public String getCommDate() {
		return this.commDate;
	}


	public void setCommAreaCode(String commAreaCode) {
		this.commAreaCode = commAreaCode;
	}

	public String getCommAreaCode() {
		return this.commAreaCode;
	}


	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCallType() {
		return this.callType;
	}


	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return this.type;
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
}
