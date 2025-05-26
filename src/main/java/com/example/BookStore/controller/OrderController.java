package com.example.BookStore.controller;

import com.example.BookStore.model.Order;
import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.OrderRepository;
import com.example.BookStore.repository.RequestRepository;
import com.example.BookStore.service.OrderService;
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
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping("/books/count")
    public long getTotalVarieties() {
        return bookRepository.count();
    }

    @GetMapping("/books/out-of-stock/count")
    public long getOutOfStockCount() {
        return bookRepository.countByStock(0);
    }

    @GetMapping("/orders/count")
    public long getTotalOrdersCount() {
        return orderRepository.count();
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalOrders() {
        long total = orderService.getTotalOrders();
        return ResponseEntity.ok(total);
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        Order savedOrder = orderService.placeOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getOrdersByEmail(@RequestParam String email) {
        List<Order> orders = orderRepository.findByUser_Email(email); // ✅ updated method call
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user-orders/{email}")
    public ResponseEntity<List<Order>> getOrdersByUserEmail(@PathVariable String email) {
        List<Order> orders = orderRepository.findByUser_Email(email); // ✅ updated method call
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @GetMapping("/status")
    public ResponseEntity<List<Order>> getOrdersByStatus(@RequestParam String status) {
        return ResponseEntity.ok(orderRepository.findByStatus(status));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(body.get("status"));
            orderRepository.save(order);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);
        return ResponseEntity.ok("Order status updated to: " + status);
    }
}