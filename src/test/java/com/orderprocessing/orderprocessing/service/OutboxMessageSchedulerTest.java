package com.orderprocessing.orderprocessing.service;

import com.orderprocessing.orderprocessing.entity.OrderEventStatus;
import com.orderprocessing.orderprocessing.entity.OrderOutbox;
import com.orderprocessing.orderprocessing.repository.OrderOutboxRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutboxMessageSchedulerTest
{

    @Mock
    private OrderOutboxRepository outboxRepository;

    @Mock
    private OrderMessageProducer messageProducer;

    @InjectMocks
    private OutboxMessageScheduler scheduler;


    @Test
    void publishPendingMessages_shouldSendAndMarkAsSent()
    {
        // Given
        OrderOutbox outbox1 = new OrderOutbox("payload-1", OrderEventStatus.PENDING);
        outbox1.setId(1L);
        when(outboxRepository.findByStatus(OrderEventStatus.PENDING)).thenReturn(List.of(outbox1));

        // When
        scheduler.publishPendingMessages();

        // Then
        verify(messageProducer).sendOrder("payload-1");
        assertEquals(OrderEventStatus.SENT, outbox1.getStatus());
        verify(outboxRepository).save(outbox1);
    }

    @Test
    void publishPendingMessages_shouldHandleFailuresGracefully()
    {
        // Given
        OrderOutbox outbox2 = new OrderOutbox("payload-2", OrderEventStatus.PENDING);
        outbox2.setId(2L);
        when(outboxRepository.findByStatus(OrderEventStatus.PENDING)).thenReturn(List.of(outbox2));
        doThrow(new RuntimeException("Queue down")).when(messageProducer).sendOrder("payload-2");

        // When
        scheduler.publishPendingMessages();

        // Then
        assertEquals(OrderEventStatus.PENDING, outbox2.getStatus()); // stays the same
        verify(outboxRepository).save(outbox2);
    }
}
