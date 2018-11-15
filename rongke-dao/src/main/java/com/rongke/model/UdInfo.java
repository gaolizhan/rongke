package com.rongke.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rongke.utils.DateJsonDeserializer;
import com.rongke.utils.DateJsonSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.util.Date;

@TableName("ud_info")
public class UdInfo {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(value = "ud_info_id")
    private String udInfoId;

    private String udOrderNo;

    private Long userId;

    @JsonSerialize(using = DateJsonSerializer.class)
    @JsonDeserialize(using = DateJsonDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String userFeatureType;

    private String userFeatureTypeStr;

    private String actualLoanPlatformCount;

    @TableField(value = "actual_loan_platform_count_1m")
    private String actualLoanPlatformCount1m;

    @TableField(value = "actual_loan_platform_count_3m")
    private String actualLoanPlatformCount3m;

    @TableField(value = "actual_loan_platform_count_6m")
    private String actualLoanPlatformCount6m;

    private String loanPlatformCount;

    @TableField(value = "loan_platform_count_1m")
    private String loanPlatformCount1m;

    @TableField(value = "loan_platform_count_3m")
    private String loanPlatformCount3m;

    @TableField(value = "loan_platform_count_6m")
    private String loanPlatformCount6m;

    private String repaymentPlatformCount;

    @TableField(value = "repayment_platform_count_1m")
    private String repaymentPlatformCount1m;

    @TableField(value = "repayment_platform_count_3m")
    private String repaymentPlatformCount3m;

    @TableField(value = "repayment_platform_count_6m")
    private String repaymentPlatformCount6m;

    private String repaymentTimesCount;

    private String score;

    private String riskEvaluation;

    public String getUdInfoId() {
        return udInfoId;
    }

    public void setUdInfoId(String udInfoId) {
        this.udInfoId = udInfoId == null ? null : udInfoId.trim();
    }

    public String getUdOrderNo() {
        return udOrderNo;
    }

    public void setUdOrderNo(String udOrderNo) {
        this.udOrderNo = udOrderNo == null ? null : udOrderNo.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserFeatureType() {
        return userFeatureType;
    }

    public void setUserFeatureType(String userFeatureType) {
        this.userFeatureType = userFeatureType == null ? null : userFeatureType.trim();
    }

    public String getUserFeatureTypeStr() {
        return userFeatureTypeStr;
    }

    public void setUserFeatureTypeStr(String userFeatureTypeStr) {
        this.userFeatureTypeStr = userFeatureTypeStr == null ? null : userFeatureTypeStr.trim();
    }

    public String getActualLoanPlatformCount() {
        return actualLoanPlatformCount;
    }

    public void setActualLoanPlatformCount(String actualLoanPlatformCount) {
        this.actualLoanPlatformCount = actualLoanPlatformCount == null ? null : actualLoanPlatformCount.trim();
    }

    public String getActualLoanPlatformCount1m() {
        return actualLoanPlatformCount1m;
    }

    public void setActualLoanPlatformCount1m(String actualLoanPlatformCount1m) {
        this.actualLoanPlatformCount1m = actualLoanPlatformCount1m == null ? null : actualLoanPlatformCount1m.trim();
    }

    public String getActualLoanPlatformCount3m() {
        return actualLoanPlatformCount3m;
    }

    public void setActualLoanPlatformCount3m(String actualLoanPlatformCount3m) {
        this.actualLoanPlatformCount3m = actualLoanPlatformCount3m == null ? null : actualLoanPlatformCount3m.trim();
    }

    public String getActualLoanPlatformCount6m() {
        return actualLoanPlatformCount6m;
    }

    public void setActualLoanPlatformCount6m(String actualLoanPlatformCount6m) {
        this.actualLoanPlatformCount6m = actualLoanPlatformCount6m == null ? null : actualLoanPlatformCount6m.trim();
    }

    public String getLoanPlatformCount() {
        return loanPlatformCount;
    }

    public void setLoanPlatformCount(String loanPlatformCount) {
        this.loanPlatformCount = loanPlatformCount == null ? null : loanPlatformCount.trim();
    }

    public String getLoanPlatformCount1m() {
        return loanPlatformCount1m;
    }

    public void setLoanPlatformCount1m(String loanPlatformCount1m) {
        this.loanPlatformCount1m = loanPlatformCount1m == null ? null : loanPlatformCount1m.trim();
    }

    public String getLoanPlatformCount3m() {
        return loanPlatformCount3m;
    }

    public void setLoanPlatformCount3m(String loanPlatformCount3m) {
        this.loanPlatformCount3m = loanPlatformCount3m == null ? null : loanPlatformCount3m.trim();
    }

    public String getLoanPlatformCount6m() {
        return loanPlatformCount6m;
    }

    public void setLoanPlatformCount6m(String loanPlatformCount6m) {
        this.loanPlatformCount6m = loanPlatformCount6m == null ? null : loanPlatformCount6m.trim();
    }

    public String getRepaymentPlatformCount() {
        return repaymentPlatformCount;
    }

    public void setRepaymentPlatformCount(String repaymentPlatformCount) {
        this.repaymentPlatformCount = repaymentPlatformCount == null ? null : repaymentPlatformCount.trim();
    }

    public String getRepaymentPlatformCount1m() {
        return repaymentPlatformCount1m;
    }

    public void setRepaymentPlatformCount1m(String repaymentPlatformCount1m) {
        this.repaymentPlatformCount1m = repaymentPlatformCount1m == null ? null : repaymentPlatformCount1m.trim();
    }

    public String getRepaymentPlatformCount3m() {
        return repaymentPlatformCount3m;
    }

    public void setRepaymentPlatformCount3m(String repaymentPlatformCount3m) {
        this.repaymentPlatformCount3m = repaymentPlatformCount3m == null ? null : repaymentPlatformCount3m.trim();
    }

    public String getRepaymentPlatformCount6m() {
        return repaymentPlatformCount6m;
    }

    public void setRepaymentPlatformCount6m(String repaymentPlatformCount6m) {
        this.repaymentPlatformCount6m = repaymentPlatformCount6m == null ? null : repaymentPlatformCount6m.trim();
    }

    public String getRepaymentTimesCount() {
        return repaymentTimesCount;
    }

    public void setRepaymentTimesCount(String repaymentTimesCount) {
        this.repaymentTimesCount = repaymentTimesCount == null ? null : repaymentTimesCount.trim();
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score == null ? null : score.trim();
    }

    public String getRiskEvaluation() {
        return riskEvaluation;
    }

    public void setRiskEvaluation(String riskEvaluation) {
        this.riskEvaluation = riskEvaluation == null ? null : riskEvaluation.trim();
    }
}