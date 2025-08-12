package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.base.R;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.BizException;
import com.example.demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public UserEntity findByUsername(String username) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getUsername, username)
        );
    }

    private final PasswordEncoder encoder;

    public void register(String username, String rawPassword) {
        if (userMapper.selectCount(
                new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, username)) > 0) {
            throw new BizException("用户名已存在");
        }
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encoder.encode(rawPassword));
        user.setRole("ROLE_USER");
        userMapper.insert(user);
    }
}