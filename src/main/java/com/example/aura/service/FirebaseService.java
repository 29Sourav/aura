package com.example.aura.service;


import com.example.aura.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    private final Firestore db;

    public FirebaseService() {
        try {
            this.db = FirestoreClient.getFirestore();
            System.out.println("✅ Firebase Firestore initialized successfully");
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize Firestore: " + e.getMessage());
            throw new RuntimeException("Failed to initialize Firebase Firestore", e);
        }
    }

    public void saveUser(User user) {
        try {
            DocumentReference docRef = db.collection("users").document(user.getUserId());
            ApiFuture<WriteResult> result = docRef.set(user);
            result.get();
            System.out.println("✅ User saved successfully: " + user.getEmail());
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    public User getUserById(String userId) {
        try {
            DocumentReference docRef = db.collection("users").document(userId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                return document.toObject(User.class);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user", e);
        }
    }

    public User getUserByEmail(String email) {
        try {
            CollectionReference users = db.collection("users");
            Query query = users.whereEqualTo("email", email);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            if (!documents.isEmpty()) {
                return documents.get(0).toObject(User.class);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user by email", e);
        }
    }

    public void addFriend(String userId, String friendUserId) {
        try {
            User user = getUserById(userId);
            User friend = getUserById(friendUserId);

            if (user == null || friend == null) {
                throw new RuntimeException("User or friend not found");
            }

            List<String> userFriends = user.getFriendIds() != null ? user.getFriendIds() : new ArrayList<>();
            if (!userFriends.contains(friendUserId)) {
                userFriends.add(friendUserId);
                user.setFriendIds(userFriends);

                DocumentReference userRef = db.collection("users").document(userId);
                userRef.update("friendIds", userFriends);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error adding friend", e);
        }
    }

    public List<User> getFriends(String userId) {
        try {
            User user = getUserById(userId);
            if (user == null || user.getFriendIds() == null) {
                return new ArrayList<>();
            }

            List<User> friends = new ArrayList<>();
            for (String friendId : user.getFriendIds()) {
                User friend = getUserById(friendId);
                if (friend != null) {
                    friends.add(friend);
                }
            }
            return friends;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching friends", e);
        }
    }
}
