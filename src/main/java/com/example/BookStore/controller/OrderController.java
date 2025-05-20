package com.example.BookStore.controller;

import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.OrderRepository;
import com.example.BookStore.repository.RequestRepository;
import com.example.BookStore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        long total = orderService.getTotalOrders(); // You must implement this in service
        return ResponseEntity.ok(total);
    }
}
