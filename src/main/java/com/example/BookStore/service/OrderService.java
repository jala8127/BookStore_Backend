package com.example.BookStore.service;

import com.example.BookStore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;  // Inject repository here

    public long getTotalOrders() {
        return orderRepository.count(); // Use JPA repository count method


    }
}
