package com.hfh.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * <p>
 * Class description goes here.
 * </p>
 *
 * @author 何福海
 * @version 1.0
 * @since 2025/7/22
 */
@Configuration
public class RedisScriptConfig {

    @Bean
    public RedisScript<Long> stockScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("redis/stockDeduct.lua")); // 确保路径正确
        script.setResultType(Long.class);
        return script;
    }

}
