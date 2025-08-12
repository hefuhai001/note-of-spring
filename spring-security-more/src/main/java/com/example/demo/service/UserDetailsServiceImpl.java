//package com.example.demo.service;
//
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.example.demo.entity.User;
//import com.example.demo.mapper.UserMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Primary;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service("userDetailsService")
//@RequiredArgsConstructor
//@Primary
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    private final UserMapper userMapper;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        User u = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
//        if (u == null) throw new UsernameNotFoundException("用户不存在");
//        return new org.springframework.security.core.userdetails.User(
//                u.getUsername(), u.getPassword(),
//                List.of(new SimpleGrantedAuthority(u.getRole()))
//        );
//    }
//}
