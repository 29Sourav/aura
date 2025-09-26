package com.example.aura.controller;


import com.example.aura.model.AddFriendRequest;
import com.example.aura.service.FirebaseService;
import com.example.aura.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/{userId}/friends")
    public ResponseEntity<?> addFriend(
            @PathVariable String userId,
            @RequestBody AddFriendRequest request,
            @RequestHeader("Authorization") String token) {

        try {
            String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
            firebaseService.addFriend(userId, request.getFriendUserId());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Friend added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<?> getFriends(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(firebaseService.getFriends(userId));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}