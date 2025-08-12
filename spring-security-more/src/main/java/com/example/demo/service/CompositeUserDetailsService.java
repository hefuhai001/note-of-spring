package com.example.demo.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.Admin;
import com.example.demo.entity.User;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompositeUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;
    private final AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 先查用户表
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(),
                    List.of(new SimpleGrantedAuthority(user.getRole()))
            );
        }

        // 2. 再查管理员表
        Admin admin = adminMapper.selectOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getUsername, username));
        if (admin != null) {
            return new org.springframework.security.core.userdetails.User(
                    admin.getUsername(), admin.getPassword(),
                    List.of(new SimpleGrantedAuthority(admin.getRole()))
            );
        }

        throw new UsernameNotFoundException("用户名不存在");
    }
}
