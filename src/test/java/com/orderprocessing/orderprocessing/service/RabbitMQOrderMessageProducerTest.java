package com.orderprocessing.orderprocessing.service;

import com.orderprocessing.orderprocessing.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RabbitMQOrderMessageProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RabbitMQConfig rabbitMQConfig;

    @InjectMocks
    private RabbitMQOrderMessageProducer producer;

    @Test
    void sendOrder_shouldSendMessageToCorrectQueue() {
        // Given
        String payload = "{\"item\":\"iPhone 15\",\"quantity\":2}";
        String queueName = "order-queue";

        when(rabbitMQConfig.getORDER_QUEUE()).thenReturn(queueName);

        // When
        producer.sendOrder(payload);

        // Then
        verify(rabbitTemplate).convertAndSend(queueName, payload);
    }
}
