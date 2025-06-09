package com.example.boot.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleModelError(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, ex.getMessage()));
    }

    //@ExceptionHandler(Exception.class)
    //public ResponseEntity<ApiResponse<?>> handleGenericError(Exception ex) {
    //    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //            .body(ApiResponse.error(500, ex.getMessage()));
    //}

}