package com.example.aura.service;

import com.google.firebase.auth.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseService {

    public UserRecord createUser(String email, String password) throws Exception {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        return FirebaseAuth.getInstance().createUser(request);
    }

    public void createUserProfile(String uid, String email) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> docData = new HashMap<>();
        docData.put("email", email);
        docData.put("friends", new String[]{});

        try {
            db.collection("users").document(uid).set(docData).get();
        } catch (InterruptedException e) {
            throw new Exception("Error creating user profile", e);
        }
    }

    public boolean verifyIdToken(String idToken) throws Exception {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        return decodedToken != null;
    }
}
