package com.example.common.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200, "成功"),
    REQUEST_ERROR(400, "参数校验失败"),
    FAILURE(500, "失败自定义消息");

    private final int code;
    private final String msg;
}
