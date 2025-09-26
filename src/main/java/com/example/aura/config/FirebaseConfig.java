package com.example.aura.config;



import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.InputStream;

@Component
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            // Check if Firebase is already initialized
            if (!FirebaseApp.getApps().isEmpty()) {
                System.out.println(" Firebase already initialized");
                return;
            }

            InputStream serviceAccount =
                    getClass().getClassLoader().getResourceAsStream("firebase-service-account.json");

            if (serviceAccount == null) {
                throw new RuntimeException(" Firebase service account file not found! " +
                        "Make sure 'firebase-service-account.json' is in src/main/resources/");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://YOUR-PROJECT-ID.firebaseio.com") // ← Replace with your Project ID
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println(" Firebase initialized successfully");

        } catch (Exception e) {
            System.err.println(" Failed to initialize Firebase: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }
}