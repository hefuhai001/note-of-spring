package com.example.hfh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        ConfigurableEnvironment environment = SpringApplication.run(ApiApplication.class, args).getEnvironment();

        log.info("""
                +----------------------------------------------------------------+
                 Application: APP is running Success!
                 Document:     http://localhost:{}/doc.html
                 Document:     /swagger-ui/index.html#/
                +----------------------------------------------------------------+
                """, environment.getProperty("server.port"));

    }

}
