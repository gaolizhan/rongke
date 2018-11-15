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
 * @SecondCatalogue
 * @二级目录(second_catalogue)
 * @version : Ver 1.0
 */
@TableName("second_catalogue")
public class SecondCatalogue implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:主键id
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * @备注:二级目录名称
	 * @字段:title VARCHAR(55)
	 */
	private String title;


	/**
	 * @备注:一级外键id
	 * @字段:first_id BIGINT(19)
	 */
	private Long firstId;
	/**
	 * 备注:一级外键ids
	 */
	@TableField(exist = false)
	private FirstCatalogue first;


	public SecondCatalogue(){
	}

	public SecondCatalogue(
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


	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setFirstId(Long firstId) {
			this.firstId = firstId;
			}

	public Long getFirstId() {
			return this.firstId;
	}
	public void setFirst(FirstCatalogue first) {
		this.first = first;
	}

	public FirstCatalogue getFirst() {
		return this.first;
	}
}
