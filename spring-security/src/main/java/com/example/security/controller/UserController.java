package com.example.security.controller;


import com.example.security.base.ApiResponse;
import com.example.security.dto.AuthRequest;
import com.example.security.entity.Role;
import com.example.security.entity.User;
import com.example.security.repository.RoleRepository;
import com.example.security.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody AuthRequest authRequest) {
        if (userRepository.findByUsername(authRequest.getUsername()) != null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Username already exists"));
        }

        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setEnabled(true);

        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        return ResponseEntity.ok(ApiResponse.success("User registered successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userRepository.findAll()));
    }

}
