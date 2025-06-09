package com.example.common.config;

import com.example.common.resp.Result;
import com.example.common.resp.ResultCode;
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
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream().map(o -> o.getDefaultMessage()).collect(Collectors.toList());

        StringBuffer sb = new StringBuffer(ResultCode.REQUEST_ERROR.getMsg());
        collect.forEach(item -> sb.append("，").append(item));
        return Result.failure(ResultCode.REQUEST_ERROR, sb.toString());
    }

    /**
     * 处理 json 请求体调用接口校验失败抛出的异常
     *
     * @param httpServletResponse
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidExceptionHandler(HttpServletResponse httpServletResponse, MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream().map(o -> o.getDefaultMessage()).collect(Collectors.toList());

        StringBuffer sb = new StringBuffer(ResultCode.REQUEST_ERROR.getMsg());
        collect.forEach(item -> sb.append("，").append(item));
        return Result.failure(ResultCode.REQUEST_ERROR, sb.toString());
    }

    /**
     * 处理单个参数校验失败抛出的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream().map(o -> o.getMessage()).collect(Collectors.toList());

        StringBuffer sb = new StringBuffer(ResultCode.REQUEST_ERROR.getMsg());
        collect.forEach(item -> sb.append("，").append(item));
        return Result.failure(ResultCode.REQUEST_ERROR, sb.toString());
    }

    /**
     * 通用异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object handle(Exception e) {
        log.error(e.getMessage(), e);
        return Result.failure(ResultCode.FAILURE, e.getMessage());
    }
}
