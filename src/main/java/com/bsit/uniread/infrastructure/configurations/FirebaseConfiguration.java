package com.bsit.uniread.infrastructure.configurations;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfiguration {

    private final Environment environment;
    private final ResourceLoader resourceLoader;

    /**
     * Currently I don't have a storage for the Book Cover, Profile Photo of users
     * This configuration only supports Firebase Cloud Messaging for push notifications
     * Also the Google authentication provider
     * @return FirebaseApp instance
     */
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream firebaseCredentials = getFirebaseCredentials();

        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(firebaseCredentials))
                .build();

        return FirebaseApp.initializeApp(firebaseOptions);
    }

    /**
     * Still on thoughts what is the scope of the realtime notification
     */
    @Bean
    public FirebaseMessaging  firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance(firebaseApp());
    }

    /**
     * Get the Firebase credentials JSON file or retrieve the credentials from the environment.
     *
     * @return InputStream of Firebase credentials
     * @throws IOException if credentials are not found
     */
    private InputStream getFirebaseCredentials() throws IOException {
        /** Retrieve the firebase_credentials through Environment variables
        * String firebaseCredentials = environment.getProperty("firebase_credentials");
        * if (firebaseCredentials != null && !firebaseCredentials.isBlank()) {
        *    return new ByteArrayInputStream(firebaseCredentials.getBytes(StandardCharsets.UTF_8));
        * }
        */

        // Retrieve the firebase_credentials through System Environment variables
        String firebaseSystemVariables = System.getenv("firebase_credentials");
        if (firebaseSystemVariables != null) {
            return new ByteArrayInputStream(firebaseSystemVariables.getBytes(StandardCharsets.UTF_8));
        }

        // Fallback to resource file
        return resourceLoader.getResource("classpath:firebase-credentials.json").getInputStream();
    }

}
