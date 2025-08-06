package com.example.jwt.config;


import com.example.jwt.resp.ApiResponse;
import com.example.jwt.resp.ApiResponseCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 统一异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理 form data方式调用接口校验失败抛出的异常
     *
     * @param e E
     * @return R
     */
    @ExceptionHandler(BindException.class)
    public ApiResponse<?> bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getDefaultMessage())
                .collect(Collectors.toList());

        StringBuffer sb = new StringBuffer(ApiResponseCode.REQUEST_ERROR.getMsg());
        collect.forEach(item -> sb.append("，").append(item));
        return ApiResponse.failure(ApiResponseCode.REQUEST_ERROR, sb.toString());
    }

    /**
     * 处理 json 请求体调用接口校验失败抛出的异常
     *
     * @param httpServletResponse R
     * @param e                   E
     * @return R
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> methodArgumentNotValidExceptionHandler(HttpServletResponse httpServletResponse, MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getDefaultMessage())
                .collect(Collectors.toList());

        StringBuffer sb = new StringBuffer(ApiResponseCode.REQUEST_ERROR.getMsg());
        collect.forEach(item -> sb.append("，").append(item));
        return ApiResponse.failure(ApiResponseCode.REQUEST_ERROR, sb.toString());
    }

    /**
     * 处理单个参数校验失败抛出的异常
     *
     * @param e E
     * @return R
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<?> constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream()
                .map(o -> o.getMessage())
                .collect(Collectors.toList());

        StringBuffer sb = new StringBuffer(ApiResponseCode.REQUEST_ERROR.getMsg());
        collect.forEach(item -> sb.append("，").append(item));
        return ApiResponse.failure(ApiResponseCode.REQUEST_ERROR, sb.toString());
    }

    /**
     * 通用异常
     *
     * @param e Exception
     * @return Object
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object handle(Exception e) {
        log.error(e.getMessage(), e);
        // 处理 Token 超时报错
        if (e.getMessage().startsWith("The Token has expired on")) {
            return ApiResponse.failure(ApiResponseCode.FAILURE, "token已过期，请重新登录");
        }
        // 处理 Token 无效
        if (e.getMessage().startsWith("The token was expected to have")) {
            return ApiResponse.failure(ApiResponseCode.FAILURE, "无效token，请重新登录");
        }
        return ApiResponse.failure(ApiResponseCode.FAILURE, e.getMessage());
    }

}

