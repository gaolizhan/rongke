package com.rongke.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by bin on 2017/8/18.
 */
@TableName("person_record")
public class PersonRecord implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * @备注:
     * @字段:id BIGINT(19)
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * @备注:用户总数
     * @字段:all_count INT(10)
     */
    private java.lang.Integer allCount;

    /**
     * @备注:放款订单总数
     * @字段:out_order_count INT(10)
     */
    private java.lang.Integer outOrderCount;

    /**
     * @备注:坏账数
     * @字段:bad_order_count INT(10)
     */
    private java.lang.Integer badOrderCount;

    /**
     * @备注:黑名单数
     * @字段:black_count INT(10)
     */
    private java.lang.Integer blackCount;

    /**
     * @备注:总注册数
     * @字段:member_count INT(10)
     */
    private java.lang.Integer memberCount;

    /**
     * @备注:总结清订单数
     * @字段:over_order_count INT(10)
     */
    private java.lang.Integer overOrderCount;

    /**
     * @备注:总放款金额
     * @字段:out_money DECIMAL(10,2)
     */
    private java.math.BigDecimal outMoney;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getOutOrderCount() {
        return outOrderCount;
    }

    public void setOutOrderCount(Integer outOrderCount) {
        this.outOrderCount = outOrderCount;
    }

    public Integer getBadOrderCount() {
        return badOrderCount;
    }

    public void setBadOrderCount(Integer badOrderCount) {
        this.badOrderCount = badOrderCount;
    }

    public Integer getBlackCount() {
        return blackCount;
    }

    public void setBlackCount(Integer blackCount) {
        this.blackCount = blackCount;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public Integer getOverOrderCount() {
        return overOrderCount;
    }

    public void setOverOrderCount(Integer overOrderCount) {
        this.overOrderCount = overOrderCount;
    }

    public BigDecimal getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(BigDecimal outMoney) {
        this.outMoney = outMoney;
    }
}
