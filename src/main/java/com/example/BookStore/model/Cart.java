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

    // Link to Book entity
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Link to User entity
    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email", nullable = false)
    private User user;

    // Snapshot fields for book details (optional, to keep history)
    private String bookTitle;
    private String bookImage;
    private double bookPrice;

    private int quantity;

    public String getUserEmail() {
        return user != null ? user.getEmail() : null;
    }

    public Cart(User user, Book book, int quantity) {
        this.user = user;
        this.book = book;
        this.bookTitle = book.getTitle();
        this.bookImage = book.getImageUrl();
        this.bookPrice = book.getPrice();
        this.quantity = quantity;
    }
}