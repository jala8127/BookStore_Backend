package com.example.BookStore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // add your order fields here (example)
    private String customerName;
    private String status; // e.g., "pending", "completed", etc.

    // Add more fields like orderDate, totalPrice, etc.

    // Getters and Setters
    // ...
}