package com.secureflow.identity.hub.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Enregistrement des tentatives de connexion
 * Login attempts recording
 *
 * Utilisé pour l'audit de sécurité et la détection d'anomalies
 * Used for security audit and anomaly detection
 */
@Data
public class LoginAttempt {

    private String email;           // Email de l'utilisateur / User email
    private String provider;        // Fournisseur utilisé / Provider used
    private LocalDateTime timestamp; // Horodatage / Timestamp
    private boolean success;        // Succès ou échec / Success or failure
    private String ipAddress;       // Adresse IP / IP address
    private String userAgent;       // Navigateur utilisé / Browser used
    private String failureReason;   // Raison de l'échec / Failure reason

    /**
     * Crée une tentative de connexion réussie
     * Creates a successful login attempt
     */
    public static LoginAttempt success(String email, String provider, String ipAddress, String userAgent) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.setEmail(email);
        attempt.setProvider(provider);
        attempt.setTimestamp(LocalDateTime.now());
        attempt.setSuccess(true);
        attempt.setIpAddress(ipAddress);
        attempt.setUserAgent(userAgent);
        return attempt;
    }

    /**
     * Crée une tentative de connexion échouée
     * Creates a failed login attempt
     */
    public static LoginAttempt failure(String email, String provider, String reason,
                                       String ipAddress, String userAgent) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.setEmail(email);
        attempt.setProvider(provider);
        attempt.setTimestamp(LocalDateTime.now());
        attempt.setSuccess(false);
        attempt.setFailureReason(reason);
        attempt.setIpAddress(ipAddress);
        attempt.setUserAgent(userAgent);
        return attempt;
    }
}