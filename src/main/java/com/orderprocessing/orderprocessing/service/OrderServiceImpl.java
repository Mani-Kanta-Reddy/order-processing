package com.orderprocessing.orderprocessing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderprocessing.orderprocessing.dto.OrderRequestDTO;
import com.orderprocessing.orderprocessing.dto.OrderResponseDTO;
import com.orderprocessing.orderprocessing.dto.PagedResponseDTO;
import com.orderprocessing.orderprocessing.entity.Order;
import com.orderprocessing.orderprocessing.entity.OrderEventStatus;
import com.orderprocessing.orderprocessing.entity.OrderOutbox;
import com.orderprocessing.orderprocessing.exception.OrderPersistenceException;
import com.orderprocessing.orderprocessing.repository.OrderOutboxRepository;
import com.orderprocessing.orderprocessing.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService
{
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final RabbitMQOrderMessageProducer orderMessageProducer;
    private final OrderOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OrderServiceImpl(OrderRepository orderRepository, RabbitMQOrderMessageProducer orderMessageProducer, OrderOutboxRepository outboxRepository, ObjectMapper objectMapper)
    {
        this.orderRepository = orderRepository;
        this.orderMessageProducer = orderMessageProducer;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @Override
    public OrderResponseDTO saveOrder(OrderRequestDTO orderRequestDTO)
    {
        try {
            Order order = mapOrderRequestDTOToOrder(orderRequestDTO);
            Order orderEntity = orderRepository.save(order);
            String payload = objectMapper.writeValueAsString(orderRequestDTO);
            OrderOutbox outbox = new OrderOutbox(payload, OrderEventStatus.PENDING);
            outboxRepository.save(outbox);
            return mapOrderToOrderResponseDTO(orderEntity);
        } catch (Exception e) {
            log.error("Unexpected error during order persistence: {}", e.getMessage(), e);
            throw new OrderPersistenceException("Unexpected error while saving order", e);
        }
    }

    @Override
    public List<OrderResponseDTO> getOrders()
    {
        List<Order> orderEntities = orderRepository.findAll();
        return orderEntities.stream()
            .map(this::mapOrderToOrderResponseDTO)
            .toList();
    }

    @Override
    public PagedResponseDTO<OrderResponseDTO> getOrders(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAll(pageable);
        List<OrderResponseDTO> dtos = orderPage
            .stream()
            .map(this::mapOrderToOrderResponseDTO)
            .toList();

        return new PagedResponseDTO<>(
            dtos,
            orderPage.getNumber(),
            orderPage.getSize(),
            orderPage.getTotalElements(),
            orderPage.getTotalPages(),
            orderPage.isLast()
        );
    }

    private Order mapOrderRequestDTOToOrder(OrderRequestDTO orderRequestDTO) {
        return new Order(orderRequestDTO.getItem(), orderRequestDTO.getQuantity());
    }

    private OrderResponseDTO mapOrderToOrderResponseDTO(Order order) {
        return new OrderResponseDTO(order.getOrderId(), order.getItem(), order.getQuantity());
    }
}
