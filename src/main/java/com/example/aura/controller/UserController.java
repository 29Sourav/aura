package com.example.aura.controller;

import com.example.aura.service.FirebaseService;
import com.example.aura.util.JwtUtil;
import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/addFriend")
    public String addFriend(@RequestHeader("Authorization") String token,
                            @RequestParam String friendUserId, Model model) {
        try {
            String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
            DocumentSnapshot userDoc = firebaseService.getUserByEmail(email);
            String userId = userDoc.getId();

            firebaseService.addFriend(userId, friendUserId);

            model.addAttribute("message", "Friend added successfully");
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "dashboard";
        }
    }
}
