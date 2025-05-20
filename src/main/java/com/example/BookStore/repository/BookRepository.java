package com.example.BookStore.repository;

import com.example.BookStore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByStock(int stock);

    // Add this method declaration to count books with a given stock value
    long countByStock(int stock);
}