package com.orderprocessing.orderprocessing.repository;

import com.orderprocessing.orderprocessing.entity.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest
{

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Should save and fetch order successfully")
    void saveAndFetchOrder()
    {
        // Given
        Order order = new Order("Laptop", 2);
        orderRepository.save(order);

        // When
        List<Order> orders = orderRepository.findAll();

        // Then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getItem()).isEqualTo("Laptop");
        assertThat(orders.get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should return paged orders")
    void fetchPagedOrders()
    {
        // Given
        orderRepository.save(new Order("Book", 1));
        orderRepository.save(new Order("Pen", 5));
        orderRepository.save(new Order("Notebook", 3));

        Pageable pageable = PageRequest.of(0, 2, Sort.by("item"));

        // When
        Page<Order> page = orderRepository.findAll(pageable);

        // Then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }
}
