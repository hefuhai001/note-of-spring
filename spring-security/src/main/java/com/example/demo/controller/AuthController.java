package com.example.demo.controller;

import com.example.demo.base.R;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
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

    @PostMapping("/login")
    public R<LoginResp> login(@RequestBody LoginReq req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        String token = jwtUtil.generateToken(auth.getName());
        return R.ok(new LoginResp(token));
    }

    @Data
    public static class LoginReq {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class LoginResp {
        private String token;
    }

    private final UserService userService;

    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody RegisterReq req) {
        userService.register(req.getUsername(), req.getPassword());
        return R.ok(null);
    }

    @Data
    public static class RegisterReq {
        @NotBlank(message = "用户名不能为空")
        private String username;
        @NotBlank(message = "密码不能为空")
        @Size(min = 4, max = 20, message = "密码长度 4-20")
        private String password;
    }

}