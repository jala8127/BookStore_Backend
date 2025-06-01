package com.example.BookStore.controller;

import com.example.BookStore.model.Order;
import com.example.BookStore.repository.OrderRepository;
import com.example.BookStore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    @Autowired
    private OrderService orderService;
    private OrderRepository orderRepository;

    // Book info
    @GetMapping("/books/total-count")
    public long getTotalBooksCount() {
        return orderService.getTotalBooksCount();
    }

    @GetMapping("/books/out-of-stock")
    public long getOutOfStockBookCount() {
        return orderService.getOutOfStockBookCount();
    }

    // Order metrics
    @GetMapping("/count")
    public long getTotalOrdersCount() {
        return orderService.getTotalOrders();
    }

    // Place new order
    @PostMapping
    public ResponseEntity<Order> placeOrder(@Valid @RequestBody Order order) {
        Order savedOrder = orderService.placeOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    // Get user orders by email
    @GetMapping("/user/{email}")
    public List<Order> getOrdersByUserEmail(@PathVariable String email) {
        return orderService.getOrdersByEmail(email);
    }

    //  Get all orders
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/status")
    public ResponseEntity<List<Order>> getOrdersByStatus(@RequestParam String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Order>> getCompletedOrders() {
        return ResponseEntity.ok(orderService.getCompletedOrders());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null) {
            return ResponseEntity.badRequest().body("Status is required.");
        }

        boolean updated = orderService.updateOrderStatus(id, status);
        if (updated) {
            return ResponseEntity.ok("Order status updated to: " + status);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}