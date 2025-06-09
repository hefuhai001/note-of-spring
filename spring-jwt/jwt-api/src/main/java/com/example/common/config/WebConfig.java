package com.example.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     *
     * @param interceptorRegistry 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(new JwtInterceptor())
                // 拦截所有请求，通过判断是否有 @JwtToken 注解 决定是否需要登录
                .addPathPatterns("/**");
    }
}


