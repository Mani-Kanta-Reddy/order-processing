package com.orderprocessing.orderprocessing.repository;

import com.orderprocessing.orderprocessing.entity.OrderEventStatus;
import com.orderprocessing.orderprocessing.entity.OrderOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, Long>
{
    List<OrderOutbox> findByStatus(OrderEventStatus status);
}
