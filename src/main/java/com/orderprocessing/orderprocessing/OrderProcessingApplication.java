package com.orderprocessing.orderprocessing;

import com.orderprocessing.orderprocessing.entity.Order;
import com.orderprocessing.orderprocessing.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class OrderProcessingApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(OrderProcessingApplication.class, args);
    }
}
