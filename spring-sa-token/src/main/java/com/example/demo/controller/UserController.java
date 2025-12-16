package com.example.demo.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.demo.mapper.RolePermissionMapper;
import com.example.demo.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiaoHe
 * @since 2025-09-30
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/userEntity")
public class UserController {

    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @SaCheckPermission("user:add")
    @PostMapping("/add")
    public SaResult add() {
        return SaResult.ok("添加用户成功");
    }

    @SaCheckPermission("user:delete")
    @DeleteMapping("/{id}")
    public SaResult delete(@PathVariable Long id) {
        return SaResult.ok("删除用户成功");
    }

    @SaCheckRole("admin")
    @GetMapping("/list")
    public SaResult list() {
        return SaResult.ok("用户列表");
    }

    @GetMapping("/listP")
    public SaResult listP(Long userId) {
        return SaResult.data(rolePermissionMapper.listPermissionByUserId(userId));
    }

    @GetMapping("/listR")
    public SaResult listR(Long userId) {
        return SaResult.data(userRoleMapper.listRoleByUserId(userId));
    }


}
