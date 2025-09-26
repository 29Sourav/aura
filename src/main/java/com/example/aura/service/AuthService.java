package com.example.aura.service;


import com.example.aura.model.LoginRequest;
import com.example.aura.model.RegisterRequest;
import com.example.aura.model.User;
import com.example.aura.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuthService {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(RegisterRequest request) {
        try {
            System.out.println("👤 Starting registration for: " + request.getEmail());

            // Check if user already exists
            User existingUser = firebaseService.getUserByEmail(request.getEmail());
            if (existingUser != null) {
                System.out.println(" User already exists: " + request.getEmail());
                throw new RuntimeException("User already exists");
            }

            // Create new user
            String userId = UUID.randomUUID().toString();
            User user = new User();
            user.setUserId(userId);
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setName(request.getName());
            user.setFriendIds(new ArrayList<>());
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());

            System.out.println(" Saving user to database...");
            firebaseService.saveUser(user);

            String token = jwtUtil.generateToken(user.getEmail());
            System.out.println(" Registration successful for: " + request.getEmail());
            return token;

        } catch (Exception e) {
            System.err.println(" Registration failed: " + e.getMessage());
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public String login(LoginRequest request) {
        try {
            System.out.println("🔐 Attempting login for: " + request.getEmail());

            User user = firebaseService.getUserByEmail(request.getEmail());

            if (user == null) {
                System.out.println(" User not found: " + request.getEmail());
                throw new RuntimeException("Invalid credentials");
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                System.out.println(" Password mismatch for: " + request.getEmail());
                throw new RuntimeException("Invalid credentials");
            }

            String token = jwtUtil.generateToken(user.getEmail());
            System.out.println("Login successful for: " + request.getEmail());
            return token;

        } catch (Exception e) {
            System.err.println(" Login failed: " + e.getMessage());
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }
}