package com.example.demo.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.User;
import com.example.demo.exception.BizException;
import com.example.demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisterService {
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    public void register(String username, String rawPassword) {
        if (userMapper.exists(Wrappers.<User>lambdaQuery().eq(User::getUsername, username)))
            throw new BizException("用户名已存在");
        userMapper.insert(
                User.builder()
                        .username(username)
                        .password(encoder.encode(rawPassword))
                        .role("ROLE_USER")
                        .build());
    }
}
