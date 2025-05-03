package com.orderprocessing.orderprocessing.service;

import com.orderprocessing.orderprocessing.dto.OrderRequestDTO;
import com.orderprocessing.orderprocessing.dto.OrderResponseDTO;
import com.orderprocessing.orderprocessing.entity.Order;
import com.orderprocessing.orderprocessing.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService
{
    private final OrderRepository orderRepository;
    private final RabbitMQOrderMessageProducer orderMessageProducer;

    public OrderServiceImpl(OrderRepository orderRepository, RabbitMQOrderMessageProducer orderMessageProducer)
    {
        this.orderRepository = orderRepository;
        this.orderMessageProducer = orderMessageProducer;
    }

    @Override
    public OrderResponseDTO saveOrder(OrderRequestDTO orderRequestDTO)
    {
        Order order = mapOrderRequestDTOToOrder(orderRequestDTO);
        Order orderEntity = orderRepository.save(order);
        orderMessageProducer.sendOrder(orderRequestDTO);
        return mapOrderToOrderResponseDTO(orderEntity);
    }

    @Override
    public List<OrderResponseDTO> getOrders()
    {
        List<Order> orderEntities = orderRepository.findAll();
        return orderEntities.stream()
            .map(this::mapOrderToOrderResponseDTO)
            .toList();
    }

    private Order mapOrderRequestDTOToOrder(OrderRequestDTO orderRequestDTO) {
        return new Order(orderRequestDTO.getItem(), orderRequestDTO.getQuantity());
    }

    private OrderResponseDTO mapOrderToOrderResponseDTO(Order order) {
        return new OrderResponseDTO(order.getOrderId(), order.getItem(), order.getQuantity());
    }
}
