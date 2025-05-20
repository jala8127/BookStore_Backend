package com.example.BookStore.model;

import jakarta.persistence.*;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookTitle;
    private String author;
    private int count; // number of requests

    // Constructors
    public Request() {}

    public Request(String bookTitle, String author, int count) {
        this.bookTitle = bookTitle;
        this.author = author;
        this.count = count;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}