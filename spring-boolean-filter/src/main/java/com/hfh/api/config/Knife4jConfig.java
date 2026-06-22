package com.hfh.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boolean Filter API")
                        .description("基于布隆过滤器的订单查询接口文档")
                        .version("1.0.0")
                        .contact(new Contact().name("hfh"))
                        .license(new License().name("Apache 2.0")));
    }
}
