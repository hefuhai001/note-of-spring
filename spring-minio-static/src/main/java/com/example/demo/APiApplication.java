package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
@SpringBootApplication
public class APiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(APiApplication.class, args);
        ConfigurableEnvironment environment = run.getEnvironment();

        String applicationName = environment.getProperty("spring.application.name", "app");
        String serverPort = environment.getProperty("server.port", "8080");

        log.info("""
                
                +----------------------------------------------------------------+
                 Application: '{}' is running Success!
                 Document:     http://localhost:{}/doc.html
                 Document:     http://localhost:{}/swagger-ui/index.html#/
                +----------------------------------------------------------------+
                """, applicationName, serverPort, serverPort);

    }

}
