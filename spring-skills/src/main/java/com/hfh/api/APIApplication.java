package com.hfh.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class APIApplication {

    public static void main(String[] args) {
        SpringApplication.run(APIApplication.class, args);
        System.out.println("=================================");
        System.out.println("  Spring Skills Application Started");
        System.out.println("  Skills API: http://localhost:8080/api/skills");
        System.out.println("=================================");
    }

}
