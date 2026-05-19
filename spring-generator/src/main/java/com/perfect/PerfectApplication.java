package com.perfect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
@SpringBootApplication
public class PerfectApplication {

    public static void main(String[] args) {
        ConfigurableEnvironment environment = SpringApplication.run(PerfectApplication.class, args).getEnvironment();
        String applicationName = environment.getProperty("spring.application.name");
        String serverPort = environment.getProperty("server.port");

        log.info("""
                
                +----------------------------------------------------------------+
                 Application: '{}' is running Success!
                 Local URL:    http://localhost:{}
                 Document:     http://localhost:{}/doc.html
                 Document:     http://localhost:{}/swagger-ui/index.html#/
                +----------------------------------------------------------------+
                """, applicationName, serverPort, serverPort, serverPort);
    }

}
