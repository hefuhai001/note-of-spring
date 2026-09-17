package com.example.hfh.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${mq.exchange.direct}")
    private String exchangeName;

    @Value("${mq.queue.default}")
    private String queueName;

    @Value("${mq.routing.key}")
    private String routingKey;

    @Bean
    public DirectExchange directExchange() {
        // durable=true: Exchange 持久化，RabbitMQ 重启后不会丢失
        // autoDelete=false: 没有队列绑定时也不自动删除
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue defaultQueue() {
        // durable=true: 队列持久化
        // exclusive=false: 允许其他连接访问
        // autoDelete=false: 没有消费者时也不自动删除
        return new Queue(queueName, true, false, false);
    }

    @Bean
    public Binding binding(Queue defaultQueue, DirectExchange directExchange) {
        // 将队列绑定到交换机，消息的 routingKey 匹配时才会路由到该队列
        return BindingBuilder.bind(defaultQueue).to(directExchange).with(routingKey);
    }
}
