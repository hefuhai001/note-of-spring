package com.example.deploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeployMvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeployMvcApplication.class, args);
        System.out.println("http://localhost:9090/");
    }

}
