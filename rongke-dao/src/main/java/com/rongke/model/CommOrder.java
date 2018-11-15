package com.rongke.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rongke.pojo.CommodityDetail;
import com.rongke.utils.DateJsonDeserializer;
import com.rongke.utils.DateJsonSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@TableName("comm_order")
public class CommOrder {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId(value = "comm_order_id")
    private String commOrderId;

    private BigDecimal realPayAmount;

    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Long userId;

    private String status;

    private BigDecimal needPayAmount;

    private BigDecimal postAmount;

    private BigDecimal commTotalAmount;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private String commInfoJson;

    @TableField(exist = false)
    private List<CommodityDetail> commodityDetails;

    public String getCommOrderId() {
        return commOrderId;
    }

    public void setCommOrderId(String commOrderId) {
        this.commOrderId = commOrderId == null ? null : commOrderId.trim();
    }

    public BigDecimal getRealPayAmount() {
        return realPayAmount;
    }

    public void setRealPayAmount(BigDecimal realPayAmount) {
        this.realPayAmount = realPayAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public BigDecimal getNeedPayAmount() {
        return needPayAmount;
    }

    public void setNeedPayAmount(BigDecimal needPayAmount) {
        this.needPayAmount = needPayAmount;
    }

    public BigDecimal getPostAmount() {
        return postAmount;
    }

    public void setPostAmount(BigDecimal postAmount) {
        this.postAmount = postAmount;
    }

    public BigDecimal getCommTotalAmount() {
        return commTotalAmount;
    }

    public void setCommTotalAmount(BigDecimal commTotalAmount) {
        this.commTotalAmount = commTotalAmount;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone == null ? null : receiverPhone.trim();
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress == null ? null : receiverAddress.trim();
    }

    public String getCommInfoJson() {
        return commInfoJson;
    }

    public void setCommInfoJson(String commInfoJson) {
        this.commInfoJson = commInfoJson == null ? null : commInfoJson.trim();
    }

    public List<CommodityDetail> getCommodityDetails() {
        return commodityDetails;
    }

    public void setCommodityDetails(List<CommodityDetail> commodityDetails) {
        this.commodityDetails = commodityDetails;
    }
}