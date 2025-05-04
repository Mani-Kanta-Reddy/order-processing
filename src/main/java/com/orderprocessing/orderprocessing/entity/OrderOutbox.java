package com.orderprocessing.orderprocessing.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_outbox_tb")
public class OrderOutbox
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    private OrderEventStatus status; // PENDING, SENT, FAILED

    private LocalDateTime createdAt = LocalDateTime.now();

    public OrderOutbox()
    {
    }

    public OrderOutbox(String payload, OrderEventStatus status)
    {
        this.payload = payload;
        this.status = status;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getPayload()
    {
        return payload;
    }

    public void setPayload(String payload)
    {
        this.payload = payload;
    }

    public OrderEventStatus getStatus()
    {
        return status;
    }

    public void setStatus(OrderEventStatus status)
    {
        this.status = status;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}
