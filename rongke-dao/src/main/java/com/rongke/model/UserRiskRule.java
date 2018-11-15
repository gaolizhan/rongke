package com.rongke.model;

import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

@TableName("user_risk_rule")
public class UserRiskRule implements Serializable {
    private String id;

    private String ruleGroup;

    private Integer ruleParam;

    private Boolean coerceSign;

    private String describe;

    private String judgeSign;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(String ruleGroup) {
        this.ruleGroup = ruleGroup == null ? null : ruleGroup.trim();
    }

    public Integer getRuleParam() {
        return ruleParam;
    }

    public void setRuleParam(Integer ruleParam) {
        this.ruleParam = ruleParam;
    }

    public Boolean getCoerceSign() {
        return coerceSign;
    }

    public void setCoerceSign(Boolean coerceSign) {
        this.coerceSign = coerceSign;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe == null ? null : describe.trim();
    }

    public String getJudgeSign() {
        return judgeSign;
    }

    public void setJudgeSign(String judgeSign) {
        this.judgeSign = judgeSign;
    }
}