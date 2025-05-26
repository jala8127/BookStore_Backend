package com.example.BookStore.controller;

import com.example.BookStore.model.CartItem;
import com.example.BookStore.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/{email}")
    public List<CartItem> getUserCart(@PathVariable String email) {
        return cartItemRepository.findByUserEmail(email);
    }

    @PostMapping("/add")
    public CartItem addToCart(@RequestBody CartItem item) {
        return cartItemRepository.save(item);
    }

    @Transactional // <-- This is the important fix
    @DeleteMapping("/remove")
    public void removeFromCart(@RequestParam String email, @RequestParam Long bookId) {
        cartItemRepository.deleteByUserEmailAndBookId(email, bookId);
    }
}