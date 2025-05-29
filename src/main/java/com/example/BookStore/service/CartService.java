package com.example.BookStore.service;

import com.example.BookStore.model.Book;
import com.example.BookStore.model.Cart;
import com.example.BookStore.model.User;
import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.CartRepository;
import com.example.BookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Adds a book to the user's cart or updates the quantity if it already exists.
     */
    public Cart addToCart(String userEmail, Long bookId, int quantity) {
        // Fetch user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch book by ID
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Check if the book is already in the user's cart
        Cart existingCartItem = cartRepository.findByUser_EmailAndBook_Id(userEmail, bookId);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            return cartRepository.save(existingCartItem);
        }

        // Create a new cart item with snapshot details from the book
        Cart newCartItem = new Cart(user, book, quantity);
        return cartRepository.save(newCartItem);
    }

    /**
     * Retrieves all cart items for a given user.
     */
    public List<Cart> getCartItemsByEmail(String email) {
        return cartRepository.findByUser_Email(email);
    }

    /**
     * Removes a specific book from the user's cart.
     */
    public void removeCartItem(String email, Long bookId) {
        Cart item = cartRepository.findByUser_EmailAndBook_Id(email, bookId);
        if (item != null) {
            cartRepository.delete(item);
        }
    }

    /**
     * Clears all items from the user's cart.
     */
    public void clearCart(String email) {
        cartRepository.deleteByUser_Email(email);
    }
}