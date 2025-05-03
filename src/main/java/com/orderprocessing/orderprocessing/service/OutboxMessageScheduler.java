package com.orderprocessing.orderprocessing.service;

import com.orderprocessing.orderprocessing.entity.OrderEventStatus;
import com.orderprocessing.orderprocessing.entity.OrderOutbox;
import com.orderprocessing.orderprocessing.repository.OrderOutboxRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxMessageScheduler
{
    private final OrderOutboxRepository outboxRepository;
    private final OrderMessageProducer messageProducer;

    public OutboxMessageScheduler(OrderOutboxRepository outboxRepository, @Qualifier("rabbitMQOrderMessageProducer") OrderMessageProducer messageProducer) {
        this.outboxRepository = outboxRepository;
        this.messageProducer = messageProducer;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishPendingMessages() {
        List<OrderOutbox> pendingMessages = outboxRepository.findByStatus(OrderEventStatus.PENDING.name());

        for (OrderOutbox outbox : pendingMessages) {
            try {
                messageProducer.sendOrder(outbox.getPayload());
                outbox.setStatus(OrderEventStatus.SENT);
                outboxRepository.save(outbox);
            } catch (Exception ex) {
                outbox.setStatus(OrderEventStatus.PENDING);
                outboxRepository.save(outbox);
            }
        }
    }

}
