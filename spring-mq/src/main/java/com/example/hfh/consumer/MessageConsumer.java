package com.example.hfh.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    // @RabbitListener: 监听指定队列，当队列中有消息时自动调用此方法
    // 默认自动 ACK（确认消费），消息处理完毕后从队列中移除
    @RabbitListener(queues = "${mq.queue.default}")
    public void receive(String message) {
        log.info("收到消息: {}", message);
    }
}
