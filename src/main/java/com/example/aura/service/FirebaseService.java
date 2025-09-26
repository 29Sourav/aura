package com.example.aura.service;

import com.google.cloud.firestore.*;
import com.google.firebase.auth.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import com.google.api.core.ApiFuture;


import java.util.*;
import java.util.concurrent.ExecutionException;

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
        docData.put("friends", new ArrayList<String>());

        db.collection("users").document(uid).set(docData).get();
    }

    public DocumentSnapshot getUserByEmail(String email) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("users").whereEqualTo("email", email).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (documents.isEmpty()) throw new Exception("User not found: " + email);
        return documents.get(0);
    }

    public void addFriend(String userId, String friendUserId) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        DocumentReference userRef = db.collection("users").document(userId);

        DocumentSnapshot document = userRef.get().get();

        if (!document.exists()) {
            throw new Exception("User not found: " + userId);
        }

        List<String> friends = (List<String>) document.get("friends");

        if (friends == null) {
            friends = new ArrayList<>();
        }

        if (friends.contains(friendUserId)) {
            throw new Exception("Already friends");
        }

        friends.add(friendUserId);

        userRef.update("friends", friends).get();
    }

    public Boolean verifyIdToken(String idToken) throws Exception {
        try {
             FirebaseAuth.getInstance().verifyIdToken(idToken);
             return true;
        } catch (Exception e) {
            throw new Exception("Invalid ID token", e);
        }
    }
}
