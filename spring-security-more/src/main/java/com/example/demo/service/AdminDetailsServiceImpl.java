//package com.example.demo.service;
//
//
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.example.demo.entity.Admin;
//import com.example.demo.mapper.AdminMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service("adminDetailsService")
//@RequiredArgsConstructor
//public class AdminDetailsServiceImpl implements UserDetailsService {
//
//    private final AdminMapper adminMapper;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        Admin a = adminMapper.selectOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getUsername, username));
//        if (a == null) throw new UsernameNotFoundException("管理员不存在");
//        return new org.springframework.security.core.userdetails.User(
//                a.getUsername(), a.getPassword(),
//                List.of(new SimpleGrantedAuthority(a.getRole()))
//        );
//    }
//}
