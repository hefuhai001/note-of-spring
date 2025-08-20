package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("欢迎访问管理员仪表板");
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createUser(@RequestBody String userInfo) {
        return ResponseEntity.ok("创建用户成功: " + userInfo);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok("删除用户成功: " + id);
    }

    @GetMapping("/settings")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> systemSettings() {
        return ResponseEntity.ok("系统设置(仅SUPER_ADMIN可访问)");
    }
}