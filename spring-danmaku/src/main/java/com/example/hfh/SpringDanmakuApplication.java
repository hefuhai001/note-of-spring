package com.example.hfh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.hfh.mapper")
public class SpringDanmakuApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDanmakuApplication.class, args);
    }

}
