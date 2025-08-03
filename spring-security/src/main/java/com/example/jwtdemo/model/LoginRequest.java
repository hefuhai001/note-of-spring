package com.example.jwtdemo.model;

import lombok.Data;

/**
 * public record LoginRequest(String username, String password) {
 * }
 *
 *
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
}

