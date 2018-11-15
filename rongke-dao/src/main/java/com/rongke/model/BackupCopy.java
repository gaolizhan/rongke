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
 * @BackupCopy
 * @(backup_copy)
 * @version : Ver 1.0
 */
@TableName("backup_copy")
public class BackupCopy implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:
	 * @字段:id INT(10)
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;


	/**
	 * @备注:文件名
	 * @字段:name VARCHAR(255)
	 */
	private String name;


	/**
	 * @备注:文件地址
	 * @字段:address VARCHAR(255)
	 */
	private String address;


	/**
	 * @备注:生成时间
	 * @字段:gmt_datetime DATETIME(19)
	 */
	@JsonSerialize(using=DateJsonSerializer.class)
	@JsonDeserialize(using=DateJsonDeserializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	private java.util.Date gmtDatetime = new java.util.Date();


	public BackupCopy(){
	}

	public BackupCopy(
		Integer id
	){
		this.id = id;
	}



	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}


	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}


	public void setGmtDatetime(java.util.Date gmtDatetime) {
		this.gmtDatetime = gmtDatetime;
	}

	public java.util.Date getGmtDatetime() {
		return this.gmtDatetime;
	}
}
