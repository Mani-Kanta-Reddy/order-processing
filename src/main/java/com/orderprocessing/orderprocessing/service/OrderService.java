package com.orderprocessing.orderprocessing.service;

import com.orderprocessing.orderprocessing.dto.OrderRequestDTO;
import com.orderprocessing.orderprocessing.dto.OrderResponseDTO;
import com.orderprocessing.orderprocessing.dto.PagedResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService
{
    OrderResponseDTO saveOrder(OrderRequestDTO orderRequestDTO);

    List<OrderResponseDTO> getOrders();

    PagedResponseDTO<OrderResponseDTO> getOrders(int page, int size);
}
