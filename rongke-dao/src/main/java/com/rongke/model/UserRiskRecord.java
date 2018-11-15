package com.rongke.model;

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

@TableName("user_risk_record")
public class UserRiskRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long loanId;

    private String description;

    private String riskStatus;

    private String userRiskRuleId;

    @JsonSerialize(using=DateJsonSerializer.class)
    @JsonDeserialize(using=DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date createTime;

    private String value;

    private String ruleGroup;

    private Boolean coerceSign;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus == null ? null : riskStatus.trim();
    }

    public String getUserRiskRuleId() {
        return userRiskRuleId;
    }

    public void setUserRiskRuleId(String userRiskRuleId) {
        this.userRiskRuleId = userRiskRuleId == null ? null : userRiskRuleId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(String ruleGroup) {
        this.ruleGroup = ruleGroup == null ? null : ruleGroup.trim();
    }

    public Boolean getCoerceSign() {
        return coerceSign;
    }

    public void setCoerceSign(Boolean coerceSign) {
        this.coerceSign = coerceSign;
    }
}