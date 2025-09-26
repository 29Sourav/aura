package com.example.aura.service;

import com.example.aura.model.LoginRequest;
import com.example.aura.model.RegisterRequest;
import com.example.aura.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private JwtUtil jwtUtil;

    private final String FIREBASE_API_KEY = "AIzaSyDp2W8xQc-cB8jQArF_l1gON8rLEt8yMzc";

    public void register(RegisterRequest request) throws Exception {
        var userRecord = firebaseService.createUser(request.getEmail(), request.getPassword());
        firebaseService.createUserProfile(userRecord.getUid(), request.getEmail());
    }

    public String login(LoginRequest request) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_API_KEY;

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", request.getEmail());
        payload.put("password", request.getPassword());
        payload.put("returnSecureToken", true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String idToken = (String) response.getBody().get("idToken");

            if (firebaseService.verifyIdToken(idToken)) {
                return jwtUtil.generateToken(request.getEmail());
            }
        }
        throw new Exception("Invalid credentials");
    }
}
