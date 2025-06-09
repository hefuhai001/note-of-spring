package com.example.demo.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    private DataInitializer dataInitializer;

    @Bean
    public CommandLineRunner init() {
        return args -> dataInitializer.init();
    }
}

