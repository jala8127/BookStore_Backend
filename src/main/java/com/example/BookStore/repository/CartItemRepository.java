package com.example.BookStore.repository;

import com.example.BookStore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserEmail(String email);

    void deleteByUserEmailAndBookId(String email, Long bookId);
}