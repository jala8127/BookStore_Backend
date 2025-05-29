package com.example.BookStore.controller;

import com.example.BookStore.model.Book;
import com.example.BookStore.model.Cart;
import com.example.BookStore.model.User;
import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.UserRepository;
import com.example.BookStore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    /** Get all cart items for a user by email */
    @GetMapping("/{email}")
    public ResponseEntity<?> getCartItems(@PathVariable String email) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
            List<Cart> carts = cartService.getCartItemsByEmail(decodedEmail);
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to fetch cart items for email: " + email);
        }
    }

    /** Get user by email */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
            Optional<User> userOpt = userRepository.findByEmail(decodedEmail);

            return userOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404).body("User not found"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching user");
        }
    }

    /** Add or update item in cart */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> cartData) {
        try {
            String userEmail = (String) cartData.get("userEmail");
            Integer quantity = (Integer) cartData.get("quantity");
            Number bookIdNum = (Number) cartData.get("bookId");

            if (userEmail == null || bookIdNum == null || quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().body("Invalid input data");
            }

            Long bookId = bookIdNum.longValue();
            String decodedEmail = URLDecoder.decode(userEmail, StandardCharsets.UTF_8);

            Optional<User> userOpt = userRepository.findByEmail(decodedEmail);
            Optional<Book> bookOpt = bookRepository.findById(bookId);

            if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");
            if (bookOpt.isEmpty()) return ResponseEntity.badRequest().body("Book not found");

            // Use CartService method that accepts userEmail, bookId, and quantity
            Cart savedCart = cartService.addToCart(decodedEmail, bookId, quantity);

            return ResponseEntity.ok(savedCart);

        } catch (Exception e) {
            e.printStackTrace(); // Optional: remove for production
            return ResponseEntity.internalServerError().body("Failed to add item to cart");
        }
    }

    /** Remove a specific cart item */
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeCartItem(@RequestParam String email, @RequestParam Long bookId) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
            cartService.removeCartItem(decodedEmail, bookId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to remove cart item");
        }
    }

    /** Clear all cart items for a specific user */
    @DeleteMapping("/clear/{email}")
    public ResponseEntity<?> clearCart(@PathVariable String email) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
            cartService.clearCart(decodedEmail);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to clear cart");
        }
    }
}