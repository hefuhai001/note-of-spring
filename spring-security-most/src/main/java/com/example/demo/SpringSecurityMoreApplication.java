package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class SpringSecurityMoreApplication {

    private static final Logger log = LoggerFactory.getLogger(SpringSecurityMoreApplication.class);

    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(SpringSecurityMoreApplication.class, args).getEnvironment();
        String applicationName = env.getProperty("spring.application.name");
        String serverPort = env.getProperty("server.port");

        log.info("""
                
                +--------------------------------------------------------+
                 Application: '{}' is running Success!
                 Local URL:    http://localhost:{}
                 Document:     http://localhost:{}/doc.html
                +--------------------------------------------------------+
                """, applicationName, serverPort, serverPort
        );
    }

}
