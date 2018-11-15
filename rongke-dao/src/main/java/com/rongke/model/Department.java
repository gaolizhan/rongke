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
 * @Department
 * @(department)
 * @version : Ver 1.0
 */
@TableName("department")
public class Department implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:主键id
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * @备注:部门名称
	 * @字段:department VARCHAR(255)
	 */
	private String department;


	public Department(){
	}

	public Department(
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


	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDepartment() {
		return this.department;
	}
}
