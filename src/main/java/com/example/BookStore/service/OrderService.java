package com.example.BookStore.service;

import com.example.BookStore.model.Order;
import com.example.BookStore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public long getTotalOrders() {
        return orderRepository.count();
    }

    public Order placeOrder(Order order) {
        order.setStatus("Pending");
        return orderRepository.save(order);
    }

    // other service methods...
}