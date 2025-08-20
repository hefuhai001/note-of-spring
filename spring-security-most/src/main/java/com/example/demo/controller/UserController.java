package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> userProfile() {
        return ResponseEntity.ok("用户个人资料");
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> updateProfile(@RequestBody String profile) {
        return ResponseEntity.ok("更新个人资料: " + profile);
    }

    @GetMapping("/vip")
    @PreAuthorize("hasRole('VIP')")
    public ResponseEntity<String> vipContent() {
        return ResponseEntity.ok("VIP专属内容");
    }

    @GetMapping("/documents/{docId}")
    @PreAuthorize("@documentPermissionChecker.canViewDocument(authentication.name, #docId)")
    public ResponseEntity<String> viewDocument(@PathVariable String docId) {
        return ResponseEntity.ok("查看文档: " + docId);
    }
}