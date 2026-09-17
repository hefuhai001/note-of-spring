package com.example.hfh.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hfh.entity.User;
import com.example.hfh.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
