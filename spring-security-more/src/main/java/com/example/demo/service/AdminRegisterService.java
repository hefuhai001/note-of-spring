package com.example.demo.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.Admin;
import com.example.demo.exception.BizException;
import com.example.demo.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminRegisterService {
    private final AdminMapper adminMapper;
    private final PasswordEncoder encoder;

    public void register(String username, String rawPassword) {
        if (adminMapper.exists(Wrappers.<Admin>lambdaQuery().eq(Admin::getUsername, username)))
            throw new BizException("管理员用户名已存在");
        adminMapper.insert(
                Admin.builder()
                        .username(username)
                        .password(encoder.encode(rawPassword))
                        .role("ROLE_ADMIN")
                        .build());
    }
}