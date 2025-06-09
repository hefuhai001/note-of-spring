package com.example.wxlogin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CustomCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true); // 允许发送Cookie
        corsConfiguration.addAllowedOriginPattern("*"); // 允许所有来源
        corsConfiguration.addAllowedHeader("*"); // 允许所有请求头
        corsConfiguration.addAllowedMethod("*"); // 允许所有HTTP方法

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // 将CORS配置应用到所有路径

        return new CorsFilter(source);
    }
}
