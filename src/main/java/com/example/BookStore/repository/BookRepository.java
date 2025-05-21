package com.example.BookStore.repository;

import com.example.BookStore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find all books that have a specific stock quantity
    List<Book> findByStock(int stock);

    // Count how many books have a specific stock quantity
    long countByStock(int stock);

    // Find books by partial title match (case-insensitive)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Find books by partial author match (case-insensitive)
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Find books by category
    List<Book> findByCategory(String category);

    // Find books by genre
    List<Book> findByGenre(String genre);

    // Check if a book exists by title (case-insensitive)
    boolean existsByTitleIgnoreCase(String title);
}