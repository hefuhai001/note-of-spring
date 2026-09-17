package com.example.hfh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableAsync;

import static cn.dev33.satoken.SaManager.log;

@EnableAsync
@SpringBootApplication
@MapperScan("com.example.hfh.mapper")
public class ApiApplication {

    public static void main(String[] args) {
        ConfigurableEnvironment environment = SpringApplication.run(ApiApplication.class, args).getEnvironment();
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
