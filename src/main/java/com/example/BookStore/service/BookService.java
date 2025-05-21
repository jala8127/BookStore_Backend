package com.example.BookStore.service;

import com.example.BookStore.model.Book;
import com.example.BookStore.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private static final String UPLOAD_DIR = "uploads/images/";

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        optionalBook.ifPresent(book -> {
            deleteImageFile(book.getImageUrl());
            bookRepository.deleteById(id);
        });
    }

    public List<Book> getOutOfStockBooks() {
        return bookRepository.findByStock(0);
    }

    // Add book with image
    public Book addBookWithImage(Book book, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImageFile(imageFile);
            book.setImageUrl(fileName); // only the file name, not full path
            logger.info("Saved image file: {}", fileName);
        }
        return bookRepository.save(book);
    }

    // Update book with image
    public Book updateBookWithImage(Book book, MultipartFile imageFile) throws IOException {
        Optional<Book> optionalBook = bookRepository.findById(book.getId());
        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();

            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setPrice(book.getPrice());
            existingBook.setStock(book.getStock());
            existingBook.setGenre(book.getGenre());
            existingBook.setCategory(book.getCategory());
            existingBook.setDescription(book.getDescription());

            if (imageFile != null && !imageFile.isEmpty()) {
                deleteImageFile(existingBook.getImageUrl());
                String fileName = saveImageFile(imageFile);
                existingBook.setImageUrl(fileName);
                logger.info("Updated image file for book ID {}: {}", existingBook.getId(), fileName);
            }

            return bookRepository.save(existingBook);
        } else {
            return addBookWithImage(book, imageFile);
        }
    }

    private String saveImageFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            logger.info("Created upload directory: {}", uploadPath.toAbsolutePath());
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IOException("Invalid file name.");
        }

        Path filePath = uploadPath.resolve(originalFileName);

        // Optional: delete if a file with same name exists to overwrite
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            logger.info("Overwriting existing file: {}", filePath.toAbsolutePath());
        }

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("Saved file {} to {}", originalFileName, filePath.toAbsolutePath());

        return originalFileName;  // save original file name in DB
    }

    // Delete image from disk
    private void deleteImageFile(String fileName) {
        if (fileName != null && !fileName.isBlank()) {
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            try {
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    logger.info("Deleted image file: {}", filePath.toAbsolutePath());
                }
            } catch (IOException e) {
                logger.error("Failed to delete image file: " + filePath.toAbsolutePath(), e);
            }
        }
    }

    // Extract file extension
    private String getFileExtension(String fileName) {
        if (fileName == null) return "png";
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1 || lastDot == fileName.length() - 1) return "png";
        return fileName.substring(lastDot + 1);
    }
}