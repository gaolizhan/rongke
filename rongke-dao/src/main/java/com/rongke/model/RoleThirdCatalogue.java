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
 * @RoleThirdCatalogue
 * @角色-功能关联(role_third_catalogue)
 * @version : Ver 1.0
 */
@TableName("role_third_catalogue")
public class RoleThirdCatalogue implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:id
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * @备注:角色id
	 * @字段:role_id BIGINT(19)
	 */
	private Long roleId;
	/**
	 * 备注:角色id
	 */
	@TableField(exist = false)
	private Role role;


	/**
	 * @备注:三级目录id
	 * @字段:third_catalogue_id BIGINT(19)
	 */
	private Long thirdCatalogueId;
	/**
	 * 备注:三级目录id
	 */
	@TableField(exist = false)
	private ThirdCatalogue thirdCatalogue;


	public RoleThirdCatalogue(){
	}

	public RoleThirdCatalogue(
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

	public void setThirdCatalogueId(Long thirdCatalogueId) {
			this.thirdCatalogueId = thirdCatalogueId;
			}

	public Long getThirdCatalogueId() {
			return this.thirdCatalogueId;
	}
	public void setThirdCatalogue(ThirdCatalogue thirdCatalogue) {
		this.thirdCatalogue = thirdCatalogue;
	}

	public ThirdCatalogue getThirdCatalogue() {
		return this.thirdCatalogue;
	}
}
