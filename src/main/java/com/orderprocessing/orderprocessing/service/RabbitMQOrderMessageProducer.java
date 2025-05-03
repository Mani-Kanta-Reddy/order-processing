package com.orderprocessing.orderprocessing.service;

import com.orderprocessing.orderprocessing.config.RabbitMQConfig;
import com.orderprocessing.orderprocessing.dto.OrderRequestDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQOrderMessageProducer implements OrderMessageProducer
{
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfig rabbitMQConfig;

    public RabbitMQOrderMessageProducer(RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig)
    {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    @Override
    public void sendOrder(OrderRequestDTO orderRequestDTO)
    {
        rabbitTemplate.convertAndSend(rabbitMQConfig.getORDER_QUEUE(), orderRequestDTO);
    }
}
