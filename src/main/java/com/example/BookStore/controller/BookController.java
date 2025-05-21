package com.example.BookStore.controller;

import com.example.BookStore.model.Book;
import com.example.BookStore.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/books")
@CrossOrigin(origins = "http://localhost:4200") // Angular frontend
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    // GET: Fetch all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        logger.info("Fetching all books");
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // GET: Fetch out-of-stock books
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<Book>> getOutOfStockBooks() {
        logger.info("Fetching out-of-stock books");
        return ResponseEntity.ok(bookService.getOutOfStockBooks());
    }

    // GET: Fetch book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        logger.info("Fetching book with ID: {}", id);
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Add book without image
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addBook(@RequestBody Book book) {
        logger.info("Adding new book (no image): {}", book.getTitle());
        Book savedBook = bookService.addBook(book);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Book added successfully");
        response.put("book", savedBook);
        return ResponseEntity.ok(response);
    }

    // POST: Add book with image
    @PostMapping(value = "/add-with-image", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> addBookWithImage(
            @RequestPart("book") Book book,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        logger.info("Adding new book with image: {}", book.getTitle());
        logger.info("Received image file: {}", imageFile != null ? imageFile.getOriginalFilename() : "None");

        Book savedBook = bookService.addBookWithImage(book, imageFile);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Book with image added successfully");
        response.put("book", savedBook);
        return ResponseEntity.ok(response);
    }

    // PUT: Update book without image
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        logger.info("Updating book without image, ID: {}", id);
        if (!bookService.getBookById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        book.setId(id);
        Book updatedBook = bookService.updateBook(book);
        return ResponseEntity.ok(updatedBook);
    }

    // PUT: Update book with image
    @PutMapping(value = "/update-with-image/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateBookWithImage(
            @PathVariable Long id,
            @RequestPart("book") Book book,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        logger.info("Updating book with image, ID: {}", id);
        logger.info("Received image file: {}", imageFile != null ? imageFile.getOriginalFilename() : "None");

        book.setId(id);
        Book updatedBook = bookService.updateBookWithImage(book, imageFile);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Book with image updated successfully");
        response.put("book", updatedBook);
        return ResponseEntity.ok(response);
    }

    // DELETE: Remove book
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id) {
        logger.info("Deleting book with ID: {}", id);
        if (bookService.getBookById(id).isPresent()) {
            bookService.deleteBook(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Book deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}