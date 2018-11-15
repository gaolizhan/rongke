package com.rongke.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class RiskDataDTO {
    private String mobile;

    private String gender;

    private String idNum;

    private String apply_time;

    private List<EmergContact> emergency_contacts;

    private Present present;

    private JSONObject rawInfo;

    private JSONObject rawReport;

    private JSONArray callRecords;

    private List<Contract> contact;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public List<EmergContact> getEmergency_contacts() {
        return emergency_contacts;
    }

    public void setEmergency_contacts(List<EmergContact> emergency_contacts) {
        this.emergency_contacts = emergency_contacts;
    }

    public Present getPresent() {
        return present;
    }

    public void setPresent(Present present) {
        this.present = present;
    }

    public JSONObject getRawInfo() {
        return rawInfo;
    }

    public void setRawInfo(JSONObject rawInfo) {
        this.rawInfo = rawInfo;
    }

    public JSONObject getRawReport() {
        return rawReport;
    }

    public void setRawReport(JSONObject rawReport) {
        this.rawReport = rawReport;
    }

    public JSONArray getCallRecords() {
        return callRecords;
    }

    public void setCallRecords(JSONArray callRecords) {
        this.callRecords = callRecords;
    }

    public List<Contract> getContact() {
        return contact;
    }

    public void setContact(List<Contract> contact) {
        this.contact = contact;
    }
}
