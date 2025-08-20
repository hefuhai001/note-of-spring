package com.example.demo.service.jwt;

import com.example.demo.entity.Admin;
import com.example.demo.entity.User;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String usernameWithType) throws UsernameNotFoundException {
        // 确保格式正确
        if (!usernameWithType.contains("|")) {
            throw new UsernameNotFoundException("无效的用户名格式，应为 username|userType");
        }

        String[] parts = usernameWithType.split("\\|");
        if (parts.length != 2) {
            throw new UsernameNotFoundException("无效的用户名格式，应为 username|userType");
        }

        String username = parts[0];
        String userType = parts[1].toLowerCase();

        try {
            if ("admin".equals(userType)) {
                Admin admin = adminMapper.findByUsername(username);
                if (admin == null) {
                    throw new UsernameNotFoundException("管理员账号不存在: " + username);
                }
                return UserDetailsImpl.buildAdmin(admin);
            } else if ("user".equals(userType)) {
                User user = userMapper.findByUsername(username);
                if (user == null) {
                    throw new UsernameNotFoundException("用户账号不存在: " + username);
                }
                return UserDetailsImpl.buildUser(user);
            } else {
                throw new UsernameNotFoundException("无效的用户类型: " + userType);
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("用户认证失败", e);
        }
    }
}
