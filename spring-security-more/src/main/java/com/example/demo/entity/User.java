package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Builder
@TableName("t_user")
@Data
public class User {
    Long id;
    String username;
    String password;
    String role;
}
