package com.hfh.api.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hfh.api.entity.User;
import com.hfh.api.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
