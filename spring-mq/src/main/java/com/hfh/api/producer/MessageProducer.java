package com.hfh.api.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    private static final Logger log = LoggerFactory.getLogger(MessageProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.exchange.direct}")
    private String exchangeName;

    @Value("${mq.routing.key}")
    private String routingKey;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String message) {
        log.info("发送消息到 Exchange: {}, RoutingKey: {}, 消息内容: {}", exchangeName, routingKey, message);
        // convertAndSend: 将消息发送到指定 Exchange，通过 routingKey 路由到对应队列
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
