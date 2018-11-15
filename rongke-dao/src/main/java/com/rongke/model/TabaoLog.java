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
 * @TabaoLog
 * @(tabao_log)
 * @version : Ver 1.0
 */
@TableName("tabao_log")
public class TabaoLog implements Serializable {
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
	 * @备注:
	 * @字段:task_id VARCHAR(255)
	 */
	private String taskId;


	/**
	 * @备注:
	 * @字段:gmt_datetime DATETIME(19)
	 */
	@JsonSerialize(using=DateJsonSerializer.class)
	@JsonDeserialize(using=DateJsonDeserializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	private java.util.Date gmtDatetime = new java.util.Date();


	public TabaoLog(){
	}

	public TabaoLog(
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


	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskId() {
		return this.taskId;
	}


	public void setGmtDatetime(java.util.Date gmtDatetime) {
		this.gmtDatetime = gmtDatetime;
	}

	public java.util.Date getGmtDatetime() {
		return this.gmtDatetime;
	}
}
