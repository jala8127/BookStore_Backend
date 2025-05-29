package com.example.BookStore.repository;

import com.example.BookStore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser_Email(String email);
    Cart findByUser_EmailAndBook_Id(String email, Long bookId);
    void deleteByUser_Email(String email);
}