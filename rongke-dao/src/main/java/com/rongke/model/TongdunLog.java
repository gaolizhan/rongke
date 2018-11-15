package com.rongke.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rongke.utils.DateJsonDeserializer;
import com.rongke.utils.DateJsonSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @TongdunLog
 * @同盾申请返回信息 (tongdun_log)
 * @version : Ver 1.0
 */
@TableName("tongdun_log")
public class TongdunLog implements Serializable{
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * @备注:
     * @字段:id BIGINT(20)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * @备注:是否成功
     * @字段:success VARCHAR(50)
     */
    private String success;

    /**
     * @备注:失败code
     * @字段:reason_code VARCHAR(50)
     */
    private String reasonCode;

    /**
     * @备注:错误详情描述
     * @字段:reason_desc VARCHAR(255)
     */
    private String reasonDesc;

    /**
     * @备注:贷前申请风险报告编号
     * @字段:report_id VARCHAR(255)
     */
    private String reportId;

    /**
     * @备注:用户id
     * @字段:user_id BIGINT(20)
     */
    private Long userId;

    /**
     * @备注:订单id
     * @字段:order_id BIGINT(20)
     */
    private Long orderId;

    /**
     * @备注:提交时间
     * @字段:submit_time DATETIME(19)
     */
    @JsonSerialize(using=DateJsonSerializer.class)
    @JsonDeserialize(using=DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    private java.util.Date submitTime = new java.util.Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }
}
