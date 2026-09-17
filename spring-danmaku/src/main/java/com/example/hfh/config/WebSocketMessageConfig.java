package com.example.hfh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * 对WebSocket消息进行压缩，减少网络传输量
 */
@Configuration
public class WebSocketMessageConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        // 启用消息压缩
        registry.setMessageSizeLimit(128 * 1024) // 消息大小限制，防止大量弹幕导致的内存问题
                .setSendBufferSizeLimit(512 * 1024) // 发送缓冲区大小限制
                .setSendTimeLimit(15 * 1000); // 发送超时限制
    }
}
