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
 * @Admin
 * @管理员(admin)
 * @version : Ver 1.0
 */
@TableName("admin")
public class Admin implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;



	/**
	 * @备注:账号
	 * @字段:user_name VARCHAR(255)
	 */
	private String userName;


	/**
	 * @备注:密码
	 * @字段:password VARCHAR(255)
	 */
	private String password;


	/**
	 * @备注:
	 * @字段:phone VARCHAR(255)
	 */
	private String phone;


	/**
	 * @备注:角色外键id
	 * @字段:role_id BIGINT(19)
	 */
	private Long roleId;
	/**
	 * 备注:角色外键id
	 */
	@TableField(exist = false)
	private Role role;


	/**
	 * @备注:
	 * @字段:token VARCHAR(255)
	 */
	private String token;


	/**
	 * 部门外键
	 */
	private Long departmentId;

	@TableField(exist = false)
	private  Department department;

	/**
	 * @备注:默认1表示正常，2表示冻结
	 * @字段:status INT(10)
	 */
	private Integer status;


	/**
	 * @备注:创建时间
	 * @字段:gmt_datetime DATETIME(19)
	 */
	@JsonSerialize(using=DateJsonSerializer.class)
	@JsonDeserialize(using=DateJsonDeserializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	private java.util.Date gmtDatetime = new java.util.Date();


	/**
	 * @备注:修改时间
	 * @字段:upt_datetime DATETIME(19)
	 */
	@JsonSerialize(using=DateJsonSerializer.class)
	@JsonDeserialize(using=DateJsonDeserializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	private java.util.Date uptDatetime = new java.util.Date();


	public Admin(){
	}

	public Admin(
		Long id
	){
		this.id = id;
	}


	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return this.userName;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setRoleId(Long roleId) {
			this.roleId = roleId;
			}

	public Long getRoleId() {
			return this.roleId;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return this.role;
	}


	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
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
