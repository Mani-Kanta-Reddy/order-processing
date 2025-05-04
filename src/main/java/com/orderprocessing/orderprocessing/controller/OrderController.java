package com.orderprocessing.orderprocessing.controller;

import com.orderprocessing.orderprocessing.dto.OrderRequestDTO;
import com.orderprocessing.orderprocessing.dto.OrderResponseDTO;
import com.orderprocessing.orderprocessing.dto.PagedResponseDTO;
import com.orderprocessing.orderprocessing.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController
{
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService)
    {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO)
    {
        log.info("Received order placement request: {}", orderRequestDTO);
        OrderResponseDTO orderResponse = orderService.saveOrder(orderRequestDTO);
        log.info("Order placed successfully with ID: {}", orderResponse.getOrderId());
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders()
    {
        log.info("Fetching all orders");
        List<OrderResponseDTO> orders = orderService.getOrders();
        log.info("Total orders fetched: {}", orders.size());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/paged")
    public ResponseEntity<PagedResponseDTO<OrderResponseDTO>> getOrders(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    )
    {
        log.info("Received request to fetch paged orders - page: {}, size: {}", page, size);
        PagedResponseDTO<OrderResponseDTO> pagedResponse = orderService.getOrders(page, size);
        log.info(
            "Fetched {} orders on page {} of {} total pages",
            pagedResponse.getData().size(),
            pagedResponse.getCurrentPage(),
            pagedResponse.getTotalPages()
        );
        return ResponseEntity.ok(pagedResponse);
    }
}
