package com.example.minio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class ApiApplication {

    private static final Logger log = LoggerFactory.getLogger(ApiApplication.class);

    public static void main(String[] args) {

        ConfigurableEnvironment environment = SpringApplication.run(ApiApplication.class, args).getEnvironment();
        String applicationName = environment.getProperty("spring.application.name");
        String serverPort = environment.getProperty("server.port");

        log.info("""
                
                +----------------------------------------------------------------+
                 Application: '{}' is running Success!
                 Document:     http://localhost:{}/doc.html
                 Document:     http://localhost:{}/swagger-ui/index.html#/
                +----------------------------------------------------------------+
                """, applicationName, 8080, 8080);

    }

}
