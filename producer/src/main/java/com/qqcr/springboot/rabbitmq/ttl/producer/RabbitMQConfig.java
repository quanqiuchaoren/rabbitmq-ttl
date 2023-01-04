package com.qqcr.springboot.rabbitmq.ttl.producer;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "topic_exchange";
    public static final String CONSUMER_ACK_QUEUE_ACK = "ttl_queue";

    // 定义队列
    @Bean(CONSUMER_ACK_QUEUE_ACK)
    public Queue consumerAckQueue_ack() {
        Map<String, Object> arguments = new HashMap<>();
        // 设置TTL设置10秒过期
        arguments.put("x-message-ttl", 50000);
        return QueueBuilder.durable(CONSUMER_ACK_QUEUE_ACK).withArguments(arguments).build();
    }

    // 定义交换机
    @Bean(EXCHANGE_NAME)
    public Exchange bootExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    /**
     * 队列和交互机绑定关系 Binding
     * 1. 知道哪个队列
     * 2. 知道哪个交换机
     * 3. routing key
     *
     * @param queue    知道哪个队列
     * @param exchange 知道哪个交换机
     * @return 交换机和队列的绑定
     */
    @Bean
    public Binding bindAck(@Qualifier(CONSUMER_ACK_QUEUE_ACK) Queue queue, @Qualifier(EXCHANGE_NAME) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("ttl.#").noargs();
    }
}
