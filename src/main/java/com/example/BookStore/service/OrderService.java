package com.example.BookStore.service;

import com.example.BookStore.model.Order;
import com.example.BookStore.model.User;
import com.example.BookStore.repository.OrderRepository;
import com.example.BookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public long getTotalOrders() {
        return orderRepository.count();
    }

    public Order placeOrder(Order order) {
        // Fetch user by email from DB to get managed entity
        String email = order.getUser() != null ? order.getUser().getEmail() : null;

        if (email == null) {
            throw new RuntimeException("User email is required to place an order");
        }

        User user = userRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        order.setUser(user);
        order.setStatus("Pending");
        return orderRepository.save(order);
    }

    // other service methods...
}