package com.example.BookStore.repository;

import com.example.BookStore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    List<Book> findByStock(int stock);


    long countByStock(int stock);


    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);


    List<Book> findByCategory(String category);

    List<Book> findByGenre(String genre);


    boolean existsByTitleIgnoreCase(String title);
}