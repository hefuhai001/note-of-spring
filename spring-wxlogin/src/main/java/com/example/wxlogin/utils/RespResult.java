package com.example.wxlogin.utils;

public class RespResult {
    // 响应状态码
    private int code;
    // 响应消息
    private String message;
    // 响应数据
    private Object data;

    // 成功的响应
    public static RespResult success(Object data) {
        return success("操作成功", data);
    }

    public static RespResult success(String message, Object data) {
        RespResult result = new RespResult();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    // 失败的响应
    public static RespResult fail(String message) {
        return fail(500, message);
    }

    public static RespResult fail(int code, String message) {
        RespResult result = new RespResult();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
