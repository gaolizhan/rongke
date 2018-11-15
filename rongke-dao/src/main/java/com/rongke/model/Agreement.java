package com.rongke.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * @version : Ver 1.0
 * @Agreement
 * @(agreement)
 */
@TableName("agreement")
public class Agreement implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * @备注:
     * @字段:id BIGINT(19)
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * @备注:内容
     * @字段:content MEDIUMTEXT(16777215)
     */
    private String content;


    /**
     * @备注:类型
     * @字段:type INT(10)
     */
    private Integer type;


    public Agreement() {
    }

    public Agreement(
            Long id
    ) {
        this.id = id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }


    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }
}
