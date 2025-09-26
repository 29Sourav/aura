package com.example.aura.controller;


import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/firebase")
    public String checkFirebase() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            return " Firebase connection is working!";
        } catch (Exception e) {
            return " Firebase connection failed: " + e.getMessage();
        }
    }

    @GetMapping("/test")
    public String test() {
        return " Spring Boot is working!";
    }
}
