package com.orderprocessing.orderprocessing.service;

import com.orderprocessing.orderprocessing.dto.OrderRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface OrderMessageProducer
{
    void sendOrder(OrderRequestDTO orderRequestDTO);
}
