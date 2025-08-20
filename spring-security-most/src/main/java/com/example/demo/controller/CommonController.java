package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common")
public class CommonController {

    @GetMapping("/public")
    public ResponseEntity<String> publicInfo() {
        return ResponseEntity.ok("公开信息，无需登录");
    }

    @GetMapping("/authenticated")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> authenticatedOnly() {
        return ResponseEntity.ok("已认证用户可访问");
    }

    @GetMapping("/admin-or-vip")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIP')")
    public ResponseEntity<String> adminOrVip() {
        return ResponseEntity.ok("ADMIN或VIP可访问");
    }
}