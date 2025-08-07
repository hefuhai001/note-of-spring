package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class SpringApplication {

    private static final Logger log = LoggerFactory.getLogger(SpringApplication.class);

    public static void main(String[] args) {

        ConfigurableEnvironment environment = org.springframework.boot.SpringApplication.run(SpringApplication.class, args).getEnvironment();
        String applicationName = environment.getProperty("spring.application.name");
        String serverPort = environment.getProperty("server.port");

        log.info("""
                
                +--------------------------------------------------------+
                 Application: '{}' is running Success!
                 Local URL:    http://localhost:{}
                 Document:     http://localhost:{}/doc.html
                +--------------------------------------------------------+
                """, applicationName, serverPort, serverPort);

    }

}
