package com.example.demo.service;

import com.example.demo.entity.dto.LoginRequest;
import com.example.demo.entity.dto.LoginResponse;
import com.example.demo.entity.dto.RegisterRequest;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    LoginResponse registerAdmin(RegisterRequest registerRequest);

    LoginResponse registerUser(RegisterRequest registerRequest);

}
