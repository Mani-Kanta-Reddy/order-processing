package com.orderprocessing.orderprocessing.service;

import com.orderprocessing.orderprocessing.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQOrderMessageProducer implements OrderMessageProducer
{
    private static final Logger log = LoggerFactory.getLogger(RabbitMQOrderMessageProducer.class);
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfig rabbitMQConfig;

    public RabbitMQOrderMessageProducer(RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig)
    {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    @Override
    public void sendOrder(String payload)
    {
        try
        {
            String queueName = rabbitMQConfig.getORDER_QUEUE();
            log.info("Sending order message to queue '{}'. Payload: {}", queueName, payload);
            rabbitTemplate.convertAndSend(queueName, payload);
            log.info("Message successfully sent to queue '{}'", queueName);
        }
        catch (AmqpException amqpException)
        {
            log.error("Failed to send message to RabbitMQ. Payload: {}. Error: {}", payload, amqpException.getMessage(), amqpException);
            throw amqpException;
        }
    }
}
