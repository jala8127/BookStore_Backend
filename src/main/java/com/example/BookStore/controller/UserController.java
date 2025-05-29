package com.example.BookStore.controller;

import com.example.BookStore.model.User;
import com.example.BookStore.repository.UserRepository;
import com.example.BookStore.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null || user.getName() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required user fields"));
        }

        userService.registerUser(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        if (loginUser.getEmail() == null || loginUser.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
        }

        Optional<User> user = userService.login(loginUser.getEmail(), loginUser.getPassword());
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or password!"));
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8.name());
            Optional<User> user = userService.findByEmail(decodedEmail);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found for email: " + decodedEmail));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to process email: " + email));
        }
    }

    @PutMapping("/update-address/{email}")
    public ResponseEntity<?> updateAddress(@PathVariable String email, @RequestBody Map<String, String> body) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8.name());
            String newAddress = body.get("address");

            if (newAddress == null || newAddress.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Address cannot be empty"));
            }

            Optional<User> userOpt = userRepository.findByEmail(decodedEmail);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setAddress(newAddress);
                userRepository.save(user);
                return ResponseEntity.ok(Map.of("message", "Address updated successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found for email: " + decodedEmail));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Something went wrong while updating address"));
        }
    }
}