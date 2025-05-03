package com.orderprocessing.orderprocessing.service;

import org.springframework.stereotype.Service;

@Service
public interface OrderMessageProducer
{
    void sendOrder(String payload);
}
