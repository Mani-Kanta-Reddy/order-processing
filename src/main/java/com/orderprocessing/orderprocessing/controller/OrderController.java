package com.orderprocessing.orderprocessing.controller;

import com.orderprocessing.orderprocessing.dto.OrderRequestDTO;
import com.orderprocessing.orderprocessing.dto.OrderResponseDTO;
import com.orderprocessing.orderprocessing.dto.PagedResponseDTO;
import com.orderprocessing.orderprocessing.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController
{
    private final OrderService orderService;

    public OrderController(OrderService orderService)
    {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO)
    {
        OrderResponseDTO orderResponse = orderService.saveOrder(orderRequestDTO);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders()
    {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("/paged")
    public ResponseEntity<PagedResponseDTO<OrderResponseDTO>> getOrders(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    )
    {
        PagedResponseDTO<OrderResponseDTO> pagedResponse = orderService.getOrders(page, size);
        return ResponseEntity.ok(pagedResponse);
    }
}
