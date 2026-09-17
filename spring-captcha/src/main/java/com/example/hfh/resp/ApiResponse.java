package com.example.hfh.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Builder(toBuilder = true)
@AllArgsConstructor
@Setter
@Getter
@Slf4j
public class ApiResponse<T> {
    /**
     * 提示信息
     */
    @Schema(description = "提示信息")
    private String message;
    /**
     * 是否成功
     */
    @Schema(description = "是否成功")
    private boolean success;
    /**
     * 返回状态码
     */
    @Schema(description = "返回状态码")
    private Integer code;
    /**
     * 数据
     */
    @Schema(description = "数据")
    private T data;

    public ApiResponse() {
    }

    public static ApiResponse success() {
        ApiResponse ApiResponse = new ApiResponse();
        ApiResponse.setSuccess(Boolean.TRUE);
        ApiResponse.setCode(ApiResponseEnum.SUCCESS.getCode());
        ApiResponse.setMessage(ApiResponseEnum.SUCCESS.getMsg());
        return ApiResponse;
    }

    public static ApiResponse success(String msg) {
        ApiResponse ApiResponse = new ApiResponse();
        ApiResponse.setMessage(msg);
        ApiResponse.setSuccess(Boolean.TRUE);
        ApiResponse.setCode(ApiResponseEnum.SUCCESS.getCode());
        return ApiResponse;
    }

    public static ApiResponse success(Object data) {
        ApiResponse ApiResponse = new ApiResponse();
        ApiResponse.setData(data);
        ApiResponse.setSuccess(Boolean.TRUE);
        ApiResponse.setCode(ApiResponseEnum.SUCCESS.getCode());
        ApiResponse.setMessage(ApiResponseEnum.SUCCESS.getMsg());
        return ApiResponse;
    }

    /**
     * 返回失败 消息
     *
     * @return Result
     */
    public static ApiResponse failure() {
        ApiResponse ApiResponse = new ApiResponse();
        ApiResponse.setSuccess(Boolean.FALSE);
        ApiResponse.setCode(ApiResponseEnum.FAILURE.getCode());
        ApiResponse.setMessage(ApiResponseEnum.FAILURE.getMsg());
        return ApiResponse;
    }

    /**
     * 返回失败 消息
     *
     * @param msg 失败信息
     * @return Result
     */
    public static ApiResponse failure(String msg) {
        ApiResponse ApiResponse = new ApiResponse();
        ApiResponse.setSuccess(Boolean.FALSE);
        ApiResponse.setCode(ApiResponseEnum.FAILURE.getCode());
        ApiResponse.setMessage(msg);
        return ApiResponse;
    }

    public static ApiResponse failure(Integer code, String msg) {
        ApiResponse ApiResponse = new ApiResponse();
        ApiResponse.setSuccess(Boolean.FALSE);
        ApiResponse.setCode(code);
        ApiResponse.setMessage(msg);
        return ApiResponse;
    }


    public static ApiResponse failure(String msg, ApiResponseEnum exceptionCode) {
        ApiResponse ApiResponse = new ApiResponse();
        ApiResponse.setMessage(msg);
        ApiResponse.setSuccess(Boolean.FALSE);
        ApiResponse.setCode(exceptionCode.getCode());
        ApiResponse.setData(exceptionCode.getMsg());
        return ApiResponse;
    }

    /**
     * 返回失败 消息
     *
     * @param exceptionCode 错误信息枚举
     * @return Result
     */
    public static ApiResponse failure(ApiResponseEnum exceptionCode) {
        ApiResponse ApiResponse = new ApiResponse();
        ApiResponse.setSuccess(Boolean.FALSE);
        ApiResponse.setCode(exceptionCode.getCode());
        ApiResponse.setMessage(exceptionCode.getMsg());
        return ApiResponse;
    }

    /**
     * 返回失败 消息
     *
     * @param exceptionCode 错误信息枚举
     * @param msg           自定义错误提示信息
     * @return Result
     */
    public static ApiResponse failure(ApiResponseEnum exceptionCode, String msg) {
        ApiResponse ApiResponse = new ApiResponse();
        ApiResponse.setMessage(msg);
        ApiResponse.setSuccess(Boolean.FALSE);
        ApiResponse.setCode(exceptionCode.getCode());
        return ApiResponse;
    }

}
