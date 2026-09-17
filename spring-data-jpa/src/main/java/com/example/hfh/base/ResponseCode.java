package com.example.hfh.base;

public enum ResponseCode {

    OK(1, "成功"),
    FAIL(500, "失败");

    public int code;
    public String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
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

}
