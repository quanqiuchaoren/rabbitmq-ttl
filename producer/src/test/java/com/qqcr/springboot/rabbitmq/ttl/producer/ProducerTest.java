package com.qqcr.springboot.rabbitmq.ttl.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTest {
    //1.注入RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 这样发送的消息，消息过了设置的TTL时间之后，队列就会直接删除这10条消息
     */
    @Test
    public void send_message_to_ttl_queue_1time() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "ttl.hello", "consumer ttl mq hello~~~");
        }
    }

    /**
     * 如果队列的ttl时间设置为50秒，
     * 本条测试用例执行后，
     * 一开始，队列中的消息为10条，
     * 第10秒，为20条，
     * 第20秒为30条，
     * 到第40秒，为50条
     * <p>
     * 之后每隔10秒，消息减少10条。
     */
    @Test
    public void send_message_to_ttl_queue_2times() throws InterruptedException {
        for (int i = 0; i < 5; i++) { // 发5次
            for (int j = 0; j < 10; j++) { // 每次发10条消息
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "ttl.hello", "consumer ttl mq hello~~~");
            }
            // 中间间隔10秒
            TimeUnit.SECONDS.sleep(10);
        }
    }
}
