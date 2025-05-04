package com.orderprocessing.orderprocessing.service;

import com.orderprocessing.orderprocessing.entity.OrderEventStatus;
import com.orderprocessing.orderprocessing.entity.OrderOutbox;
import com.orderprocessing.orderprocessing.repository.OrderOutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxMessageScheduler
{
    private static final Logger log = LoggerFactory.getLogger(OutboxMessageScheduler.class);
    private final OrderOutboxRepository outboxRepository;
    private final OrderMessageProducer messageProducer;

    public OutboxMessageScheduler(OrderOutboxRepository outboxRepository, @Qualifier("rabbitMQOrderMessageProducer") OrderMessageProducer messageProducer)
    {
        this.outboxRepository = outboxRepository;
        this.messageProducer = messageProducer;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishPendingMessages()
    {
        log.info("Checking for pending outbox messages...");
        List<OrderOutbox> pendingMessages = outboxRepository.findByStatus(OrderEventStatus.PENDING);
        log.info("Found {} pending messages", pendingMessages.size());
        for (OrderOutbox outbox : pendingMessages)
        {
            try
            {
                messageProducer.sendOrder(outbox.getPayload());
                outbox.setStatus(OrderEventStatus.SENT);
                outboxRepository.save(outbox);
                log.info("Successfully sent message for outboxId: {}", outbox.getId());
            }
            catch (Exception ex)
            {
                log.error("Failed to send message for outboxId: {}. Retrying next cycle.", outbox.getId(), ex);
                outbox.setStatus(OrderEventStatus.PENDING);
                outboxRepository.save(outbox);
            }
        }
    }
}
