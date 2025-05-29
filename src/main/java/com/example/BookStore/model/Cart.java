package com.example.BookStore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email", nullable = false)
    private User user;

    private String bookTitle;
    private String bookImageUrl;
    private double bookPrice;

    private int quantity;

    public String getUserEmail() {
        return user != null ? user.getEmail() : null;
    }

    public Cart(User user, Book book, int quantity) {
        this.user = user;
        this.book = book;
        this.bookTitle = book.getTitle();
        this.bookImageUrl = book.getImageUrl();
        this.bookPrice = book.getPrice();
        this.quantity = quantity;
    }
}