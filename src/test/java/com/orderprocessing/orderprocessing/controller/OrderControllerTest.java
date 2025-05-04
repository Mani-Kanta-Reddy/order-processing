package com.orderprocessing.orderprocessing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderprocessing.orderprocessing.dto.OrderRequestDTO;
import com.orderprocessing.orderprocessing.dto.OrderResponseDTO;
import com.orderprocessing.orderprocessing.dto.PagedResponseDTO;
import com.orderprocessing.orderprocessing.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPlaceOrder() throws Exception
    {
        OrderRequestDTO request = new OrderRequestDTO("Samsung-S23", 3);
        OrderResponseDTO response = new OrderResponseDTO(1L, "Samsung-S23", 3);

        Mockito.when(orderService.saveOrder(any(OrderRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value(1))
            .andExpect(jsonPath("$.item").value("Samsung-S23"))
            .andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    void testGetOrders() throws Exception
    {
        OrderResponseDTO order = new OrderResponseDTO(1L, "Samsung-S23", 3);
        Mockito.when(orderService.getOrders()).thenReturn(List.of(order));

        mockMvc.perform(get("/api/v1/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].orderId").value(1))
            .andExpect(jsonPath("$[0].item").value("Samsung-S23"))
            .andExpect(jsonPath("$[0].quantity").value(3));
    }

    @Test
    void testGetPagedOrders() throws Exception
    {
        OrderResponseDTO order = new OrderResponseDTO(1L, "Samsung-S23", 3);
        PagedResponseDTO<OrderResponseDTO> pagedResponse = new PagedResponseDTO<>(
            List.of(order), 0, 1, 1, 1, true
        );

        Mockito.when(orderService.getOrders(eq(0), eq(1))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/orders/paged?page=0&size=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].orderId").value(1))
            .andExpect(jsonPath("$.currentPage").value(0))
            .andExpect(jsonPath("$.pageSize").value(1))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(jsonPath("$.totalPages").value(1))
            .andExpect(jsonPath("$.last").value(true));
    }
}
