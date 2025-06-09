package com.example.deerweb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin("*")
@Slf4j
@ComponentScan(basePackages = {"com","com.system","com.example","com.example.deerweb"})
public class DeerWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeerWebApplication.class, args);
    }
}
