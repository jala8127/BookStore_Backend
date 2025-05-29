package com.example.BookStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author is required")
    @Column(nullable = false)
    private String author;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Genre is required")
    private String genre;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;

    @NotBlank(message = "Description is required")
    @Column(length = 1000)
    private String description;

    private String imageUrl;

    // --- Default constructor
    public Book() {}

    // --- Parameterized constructor
    public Book(String title, String author, String category, String genre, double price, int stock, String description, String imageUrl) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.genre = genre;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}