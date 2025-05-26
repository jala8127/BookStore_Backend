package com.example.BookStore.repository;

import com.example.BookStore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_Email(String email); // ✅ Correct method name based on nested User entity
    List<Order> findByStatus(String status);
}