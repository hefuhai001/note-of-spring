package com.example.wxlogin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 指定允许跨域的路径，/**表示所有路径
                        .allowedOrigins("http://example.com", "http://another-domain.com") // 允许的来源
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的HTTP方法
                        .allowedHeaders("*") // 允许的请求头
                        .allowCredentials(true); // 是否允许发送Cookie
            }
        };
    }
}
