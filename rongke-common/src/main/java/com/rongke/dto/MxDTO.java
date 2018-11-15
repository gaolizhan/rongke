package com.rongke.dto;

public class MxDTO {
    private String mobile;
    private String name;
    private String idcard;
    private String task_id;
    private String user_id;
    private Long timestamp;

    private String result;
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "MxDTO{" +
                "mobile='" + mobile + '\'' +
                ", name='" + name + '\'' +
                ", idcard='" + idcard + '\'' +
                ", task_id='" + task_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", timestamp=" + timestamp +
                ", result='" + result + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
