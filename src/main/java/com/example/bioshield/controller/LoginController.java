package com.example.bioshield.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200", "https://bioshield-capstone.web.app", "https://bioshield-capstone.firebaseapp.com"})
public class LoginController {

    // This is the SHA-256 Base64 hashed version of the password "admin123"
    private final String EXPECTED_HASH = "JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=";

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            String hashedInput = Base64.getEncoder().encodeToString(hashedBytes);

            if ("admin".equals(username) && EXPECTED_HASH.equals(hashedInput)) {

                return ResponseEntity.ok(Map.of("message", "Login successful", "token", "fake-jwt-token"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Security error ocurred");
        }

    }

}
