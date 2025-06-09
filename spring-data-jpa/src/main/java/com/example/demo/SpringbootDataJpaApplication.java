package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootDataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDataJpaApplication.class, args);
        System.out.println("http://localhost:8000/h2-console");
        System.out.println("http://localhost:8000/doc.html#/home");
        System.out.println("jdbc:h2:mem:testdb");
        System.out.println("org.h2.Driver");
    }

}
