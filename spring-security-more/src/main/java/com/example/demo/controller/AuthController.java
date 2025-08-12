package com.example.demo.controller;

import com.example.demo.base.R;
import com.example.demo.utils.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // 普通用户登录
    @PostMapping("/login/user")
    public R<String> loginUser(@RequestBody LoginReq req) {
        return login(req);
    }

    // 管理员登录
    @PostMapping("/login/admin")
    public R<String> loginAdmin(@RequestBody LoginReq req) {
        return login(req);
    }

    private R<String> login(LoginReq req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        return R.ok(jwtUtil.generateToken(auth.getName()));
    }

    @Data
    public static class LoginReq {
        private String username;
        private String password;
    }


}
