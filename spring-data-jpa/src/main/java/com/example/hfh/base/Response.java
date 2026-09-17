package com.example.hfh.base;

public class Response<T> {
    private int code;
    private String message;
    private T data;

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Response ok(Object object) {
        ResponseCode ok = ResponseCode.OK;
        return new Response(ok.getCode(), ok.getMessage(), object);
    }

    public static Response fail() {
        ResponseCode fail = ResponseCode.FAIL;
        return new Response(fail.getCode(), fail.getMessage());
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

    public void setData(T data) {
        this.data = data;
    }


}
