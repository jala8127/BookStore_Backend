package com.example.BookStore.repository;


import com.example.BookStore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // You can add custom methods if needed, e.g., countByStatus, findByStatus, etc.

    // Example:
    // long countByStatus(String status);
}