package com.example.hfh.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException e) {
        String errors = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        log.error("参数验证异常: {}", errors);
        return ResponseEntity.badRequest().body(
                Map.of("code", 400, "message", "参数验证失败: " + errors)
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(
                Map.of("code", 400, "message", "参数校验失败", "errors", errors)
        );
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<?> handleMailException(MailException e) {
        log.error("邮件发送异常: {}", e.getMessage());
        return ResponseEntity.badRequest().body(
                Map.of("code", 500, "message", "邮件发送失败: " + e.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(
                Map.of("code", 500, "message", "系统内部错误: " + e.getMessage())
        );
    }

}