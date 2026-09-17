package com.example.hfh.resp;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiResponseEnum {
    SUCCESS(200, "成功"),
    REQUEST_ERROR(400, "参数校验失败"),
    FAILURE(500, "系统错误");

    private final int code;
    private final String msg;
}
