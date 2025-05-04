package com.orderprocessing.orderprocessing.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig
{
    private final String ORDER_QUEUE;

    public RabbitMQConfig(@Value("${app.rabbitmq.order.queue.name}") String queueName)
    {
        ORDER_QUEUE = queueName;
    }

    @Bean
    public Queue orderQueue()
    {
        return new Queue(ORDER_QUEUE);
    }

    public String getORDER_QUEUE()
    {
        return ORDER_QUEUE;
    }
}
