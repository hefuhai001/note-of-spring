package com.example.demo.controller;

import com.example.demo.base.R;
import com.example.demo.service.AdminRegisterService;
import com.example.demo.service.UserRegisterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class RegisterController {

    private final UserRegisterService userRegisterService;
    private final AdminRegisterService adminRegisterService;

    /**
     * 普通用户注册
     */
    @PostMapping("/register/user")
    public R<Void> registerUser(@Valid @RequestBody RegisterReq req) {
        userRegisterService.register(req.getUsername(), req.getPassword());
        return R.ok(null);
    }

    /**
     * 管理员注册
     */
    @PostMapping("/register/admin")
    public R<Void> registerAdmin(@Valid @RequestBody RegisterReq req) {
        adminRegisterService.register(req.getUsername(), req.getPassword());
        return R.ok(null);
    }

    @Data
    public static class RegisterReq {
        @NotBlank
        private String username;
        @NotBlank
        @Size(min = 4, max = 20)
        private String password;
    }
}
