package com.example.BookStore.service;

import com.example.BookStore.model.Order;
import com.example.BookStore.model.User;
import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.OrderRepository;
import com.example.BookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Get total number of orders in the system.
     */
    public long getTotalOrders() {
        return orderRepository.count();
    }

    /**
     * Place a new order for a given user.
     * The Order object must include a User with a valid email.
     * Also validates other required fields like name, phone, address, etc.
     */
    public Order placeOrder(Order order) {
        String email = order.getUser() != null ? order.getUser().getEmail() : null;

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("User email is required to place an order.");
        }

        User user = userRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Validate required fields
        if (order.getCustomerName() == null || order.getCustomerName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required.");
        }
        if (order.getPhone() == null || order.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone number is required.");
        }
        if (order.getAddress() == null || order.getAddress().isBlank()) {
            throw new IllegalArgumentException("Address is required.");
        }
        if (order.getPaymentMethod() == null || order.getPaymentMethod().isBlank()) {
            throw new IllegalArgumentException("Payment method is required.");
        }
        if (order.getBookDetails() == null || order.getBookDetails().isBlank()) {
            throw new IllegalArgumentException("Book details are required.");
        }

        // Set user and status
        order.setUser(user);
        order.setStatus("Pending");

        // You can also set order timestamp here if needed (e.g., order.setOrderDate(new Date());)

        return orderRepository.save(order);
    }

    /**
     * Get orders placed by a specific user's email.
     */
    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByUser_Email(email);
    }

    /**
     * Get all orders in the system.
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Get orders by their status.
     */
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * Get all completed orders.
     */
    public List<Order> getCompletedOrders() {
        return orderRepository.findByStatus("Completed");
    }

    /**
     * Update status of an order by ID.
     * Returns true if updated, false if order not found.
     */
    public boolean updateOrderStatus(Long id, String status) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(status);
            orderRepository.save(order);
            return true;
        }).orElse(false);
    }

    /**
     * Delete an order by ID.
     * Returns true if deleted, false if order does not exist.
     */
    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Get total number of books.
     */
    public long getTotalBooksCount() {
        return bookRepository.count();
    }

    /**
     * Get count of books that are out of stock (stock = 0).
     */
    public long getOutOfStockBookCount() {
        return bookRepository.countByStock(0);
    }
}