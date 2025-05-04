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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderOutboxRepository outboxRepository;

    @Mock
    private RabbitMQOrderMessageProducer orderMessageProducer;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void saveOrder_success() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO("Samsung-S23", 3);
        Order savedOrder = new Order("Samsung-S23", 3);
        savedOrder.setOrderId(1L);

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(objectMapper.writeValueAsString(request)).thenReturn("{\"item\":\"Samsung-S23\",\"quantity\":3}");
        when(outboxRepository.save(any(OrderOutbox.class))).thenReturn(new OrderOutbox("payload", OrderEventStatus.PENDING));

        OrderResponseDTO response = orderService.saveOrder(request);

        assertEquals(1L, response.getOrderId());
        assertEquals("Samsung-S23", response.getItem());
        assertEquals(3, response.getQuantity());

        verify(orderRepository).save(any(Order.class));
        verify(outboxRepository).save(any(OrderOutbox.class));
    }

    @Test
    void saveOrder_throwsOrderPersistenceException_onJsonProcessingError() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO("Samsung-S23", 3);

        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("DB error"));

        OrderPersistenceException ex = assertThrows(OrderPersistenceException.class, () -> {
            orderService.saveOrder(request);
        });

        assertTrue(ex.getMessage().contains("Unexpected error while saving order"));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrders_returnsListOfOrders() {
        Order order = new Order("iPhone 15", 2);
        order.setOrderId(101L);

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderResponseDTO> orders = orderService.getOrders();

        assertEquals(1, orders.size());
        assertEquals("iPhone 15", orders.get(0).getItem());
    }

    @Test
    void getOrders_returnsEmptyList() {
        when(orderRepository.findAll()).thenReturn(List.of());

        List<OrderResponseDTO> orders = orderService.getOrders();

        assertTrue(orders.isEmpty());
    }

    @Test
    void getOrdersPaged_returnsPaginatedResponse() {
        Order order = new Order("MacBook Air", 1);
        order.setOrderId(999L);

        Page<Order> orderPage = new PageImpl<>(List.of(order), PageRequest.of(0, 1), 1);

        when(orderRepository.findAll(PageRequest.of(0, 1))).thenReturn(orderPage);

        PagedResponseDTO<OrderResponseDTO> response = orderService.getOrders(0, 1);

        assertEquals(1, response.getData().size());
        assertEquals("MacBook Air", response.getData().get(0).getItem());
        assertEquals(0, response.getCurrentPage());
        assertEquals(1, response.getTotalPages());
        assertTrue(response.isLast());
    }
}
