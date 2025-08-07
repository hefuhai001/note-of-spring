package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users")
    List<User> findAll();

    // 增
    int insertUser(User user);

    // 删
    int deleteUser(@Param("id") Long id);

    // 改
    int updateUser(User user);

    // 单个查
    User selectUserById(@Param("id") Long id);

    // 全部查
    List<User> selectAllUsers();

}


