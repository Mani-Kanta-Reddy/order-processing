package com.orderprocessing.orderprocessing.repository;

import com.orderprocessing.orderprocessing.entity.OrderEventStatus;
import com.orderprocessing.orderprocessing.entity.OrderOutbox;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderOutboxRepositoryTest
{

    @Autowired
    private OrderOutboxRepository outboxRepository;

    @Test
    @DisplayName("Should save and retrieve OrderOutbox entity")
    void saveAndRetrieveOutbox()
    {
        // Given
        OrderOutbox outbox = new OrderOutbox("sample-payload", OrderEventStatus.PENDING);
        outboxRepository.save(outbox);

        // When
        List<OrderOutbox> outboxes = outboxRepository.findAll();

        // Then
        assertThat(outboxes).hasSize(1);
        assertThat(outboxes.get(0).getPayload()).isEqualTo("sample-payload");
        assertThat(outboxes.get(0).getStatus()).isEqualTo(OrderEventStatus.PENDING);
    }

    @Test
    @DisplayName("Should fetch outboxes by status")
    void findByStatus()
    {
        // Given
        outboxRepository.save(new OrderOutbox("msg-1", OrderEventStatus.PENDING));
        outboxRepository.save(new OrderOutbox("msg-2", OrderEventStatus.SENT));
        outboxRepository.save(new OrderOutbox("msg-3", OrderEventStatus.PENDING));

        // When
        List<OrderOutbox> pendingOutboxes = outboxRepository.findByStatus(OrderEventStatus.PENDING);

        // Then
        assertThat(pendingOutboxes).hasSize(2);
        assertThat(pendingOutboxes)
            .extracting(OrderOutbox::getPayload)
            .containsExactlyInAnyOrder("msg-1", "msg-3");
    }
}
