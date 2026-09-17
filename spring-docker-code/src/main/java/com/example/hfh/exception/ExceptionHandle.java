package com.example.hfh.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.util.SaResult;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class ExceptionHandle {

    // 未登录
    @ExceptionHandler(NotLoginException.class)
    public SaResult handlerNotLogin(NotLoginException e) {
        return SaResult.code(401).setMsg("请先登录");
    }

    // 缺少权限
    @ExceptionHandler(NotPermissionException.class)
    public SaResult handlerNotPermission(NotPermissionException e) {
        return SaResult.code(403).setMsg("缺少权限：" + e.getPermission());
    }

    // 缺少角色
    @ExceptionHandler(NotRoleException.class)
    public SaResult handlerNotRole(NotRoleException e) {
        return SaResult.code(403).setMsg("缺少角色：" + e.getRole());
    }

}
