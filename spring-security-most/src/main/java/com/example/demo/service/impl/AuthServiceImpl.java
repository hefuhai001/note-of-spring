
package com.example.demo.service.impl;

import com.example.demo.entity.Admin;
import com.example.demo.entity.User;
import com.example.demo.entity.dto.LoginRequest;
import com.example.demo.entity.dto.LoginResponse;
import com.example.demo.entity.dto.RegisterRequest;
import com.example.demo.exception.AuthException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.AuthService;
import com.example.demo.service.jwt.JwtService;
import com.example.demo.service.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String userType = loginRequest.getUserType().toLowerCase();
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // 认证时使用 "username|userType" 格式
        String usernameWithType = username + "|" + userType;

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usernameWithType, // 使用完整格式
                            password
                    )
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);

            return LoginResponse.builder()
                    .token(jwt)
                    .username(username)
                    .role(userDetails.getRole())
                    .userType(userType)
                    .build();
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("用户名或密码错误");
        }
    }

    @Override
    @Transactional
    public LoginResponse registerAdmin(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (adminMapper.findByUsername(registerRequest.getUsername()) != null) {
            throw new UserAlreadyExistsException("管理员用户名已存在");
        }

        // 创建管理员
        Admin admin = Admin.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole() != null ? registerRequest.getRole() : "ADMIN")
                .build();

        adminMapper.insert(admin);

        // 自动登录
        return login(new LoginRequest(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                "admin"
        ));
    }

    @Override
    @Transactional
    public LoginResponse registerUser(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(registerRequest.getUsername()) != null) {
            throw new UserAlreadyExistsException("用户用户名已存在");
        }

        // 创建用户
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole() != null ? registerRequest.getRole() : "USER")
                .build();

        userMapper.insert(user);

        // 自动登录
        return login(new LoginRequest(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                "user"
        ));
    }

}