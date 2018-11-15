package com.rongke.commons;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengwenwen on 2016/10/18.
 */
public class AjaxResult {
    private Result result;
    private Map<?, ?> data;

    class Result {
        private String code;
        private String message;
        private Boolean isOk;

        public Result() {
        }

        public Result(String code, String message, Boolean isOk) {
            this.code = code;
            this.message = message;
            this.isOk = isOk;
        }

        public Result(Boolean isOk) {
            this.isOk = isOk;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getIsOk() {
            return isOk;
        }

        public void setIsOk(Boolean ok) {
            isOk = ok;
        }
    }

    public Result getResult() {
        if (result == null) {
            result = new Result();
        }
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Map<?, ?> getData() {
        if (data == null) {
            data = new HashMap<Object, Object>();
        }
        return data;
    }

    public void setData(Map<?, ?> data) {
        this.data = data;
    }

    /**
     * 返回接口调用成功及数据
     *
     * @param data
     * @return
     */
    public static AjaxResult successResult(Map<Object, Object> data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.getResult().setCode("S001");
        ajaxResult.getResult().setMessage("ok");
        ajaxResult.getResult().setIsOk(true);
        ajaxResult.setData(data);
        return ajaxResult;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
