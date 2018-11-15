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
 * @MsgModel
 * @(msg_model)
 * @version : Ver 1.0
 */
@TableName("msg_model")
public class MsgModel implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * @备注:类型 1借款成功 2还款前一天 3还款日 4还款成功 5逾期
	 * @字段:type INT(10)
	 */
	private Integer type;


	/**
	 * @备注:内容
	 * @字段:content MEDIUMTEXT(16777215)
	 */
	private String content;


	public MsgModel(){
	}

	public MsgModel(
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


	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return this.type;
	}


	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}
}
