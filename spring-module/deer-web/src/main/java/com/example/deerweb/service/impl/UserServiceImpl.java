package com.example.deerweb.service.impl;

import com.example.deerweb.entity.UserEntity;
import com.example.deerweb.mapper.UserMapper;
import com.example.deerweb.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author bestwishes0203
 * @since 2024-03-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

}
