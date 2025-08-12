package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Builder
@TableName("t_admin")
@Data
public class Admin {
    Long id;
    String username;
    String password;
    String role;
}
