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
 * @TextTemplate
 * @文本模板(text_template)
 * @version : Ver 1.0
 */
@TableName("text_template")
public class TextTemplate implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:主键id
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * @备注:文本内容
	 * @字段:text TEXT(65535)
	 */
	private String text;


	/**
	 * @备注:类型 ：1,帮助中心 2,协议 3,短信 4,消息推送模板
	 * @字段:type INT(10)
	 */
	private Integer type;


	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	/**
	 * @备注:标题
	 * @字段:title VARBINARY(55)
	 */
	private String title;


	/**
	 * @备注:状态：1,正常 2,删除
	 * @字段:status INT(10)
	 */
	private Integer status;


	public TextTemplate(){
	}

	public TextTemplate(
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


	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}


	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return this.type;
	}





	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}
}
