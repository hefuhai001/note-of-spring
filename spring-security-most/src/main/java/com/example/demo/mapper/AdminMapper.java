package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    @Select("SELECT * FROM t_admin WHERE username = #{username}")
    Admin findByUsername(String username);

    @Select("SELECT EXISTS(SELECT 1 FROM t_admin WHERE username = #{username})")
    boolean existsByUsername(String username);

}