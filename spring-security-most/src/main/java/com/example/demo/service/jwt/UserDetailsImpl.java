package com.example.demo.service.jwt;

import com.example.demo.entity.Admin;
import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private String username;  // 这里存储的是 username|userType 完整格式
    private String password;
    private String role;
    private String userType;

    public static UserDetailsImpl buildAdmin(Admin admin) {
        return new UserDetailsImpl(
                admin.getUsername() + "|admin", // 存储完整格式
                admin.getPassword(),
                admin.getRole(),
                "admin"
        );
    }

    public static UserDetailsImpl buildUser(User user) {
        return new UserDetailsImpl(
                user.getUsername() + "|user", // 存储完整格式
                user.getPassword(),
                user.getRole(),
                "user"
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}