package com.rongke.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * @ThirdCatalogue
 * @三级目录(third_catalogue)
 * @version : Ver 1.0
 */
@TableName("third_catalogue")
public class ThirdCatalogue implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:主键id
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * @备注:三级目录名称
	 * @字段:tilte VARCHAR(55)
	 */
	private String title;


	/**
	 * @备注:二级外键id
	 * @字段:second_id BIGINT(19)
	 */
	private Long secondId;
	/**
	 * 备注:二级外键id
	 */
	@TableField(exist = false)
	private SecondCatalogue second;


	public ThirdCatalogue(){
	}

	public ThirdCatalogue(
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

	public void setSecondId(Long secondId) {
			this.secondId = secondId;
			}

	public Long getSecondId() {
			return this.secondId;
	}
	public void setSecond(SecondCatalogue second) {
		this.second = second;
	}

	public SecondCatalogue getSecond() {
		return this.second;
	}
}
