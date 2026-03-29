package com.secureflow.identity.hub.service;

import com.secureflow.identity.hub.model.UserIdentityDTO;
import com.secureflow.identity.hub.model.LoginAttempt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des profils utilisateurs
 * User profile management service
 *
 * Gère le stockage et la récupération des informations utilisateur
 * Handles storage and retrieval of user information
 *
 * NOTE: En production, remplacer par une vraie base de données
 * NOTE: In production, replace with a real database
 */
@Slf4j
@Service
public class UserProfileService {

    // Stockage en mémoire (simulation base de données) / In-memory storage (database simulation)
    private final Map<String, UserIdentityDTO> userDatabase = new ConcurrentHashMap<>();
    private final List<LoginAttempt> loginHistory = new ArrayList<>();

    /**
     * Sauvegarde ou met à jour le profil utilisateur
     * Saves or updates user profile
     *
     * @param userIdentity Informations utilisateur / User information
     * @return Utilisateur sauvegardé / Saved user
     */
    public UserIdentityDTO saveOrUpdateUserProfile(UserIdentityDTO userIdentity) {
        String key = userIdentity.getEmail();

        // Si l'utilisateur existe déjà, on met à jour certaines informations
        // If user already exists, update certain information
        if (userDatabase.containsKey(key)) {
            UserIdentityDTO existing = userDatabase.get(key);
            existing.setLastLoginTime(LocalDateTime.now());
            existing.setProfilePicture(userIdentity.getProfilePicture());
            existing.setFullName(userIdentity.getFullName());

            log.info("Mise à jour profil utilisateur / User profile updated: {}", key);
            userDatabase.put(key, existing);
            return existing;
        }
        // Sinon on crée un nouvel utilisateur / Otherwise create new user
        else {
            userIdentity.setAccountCreatedAt(LocalDateTime.now());
            userIdentity.setLastLoginTime(LocalDateTime.now());

            log.info(" Nouvel utilisateur créé / New user created: {}", key);
            userDatabase.put(key, userIdentity);
            return userIdentity;
        }
    }

    /**
     * Récupère un utilisateur par son email
     * Retrieves user by email
     *
     * @param email Email de l'utilisateur / User email
     * @return Utilisateur trouvé ou null / Found user or null
     */
    public UserIdentityDTO getUserByEmail(String email) {
        return userDatabase.get(email);
    }

    /**
     * Vérifie si un utilisateur existe
     * Checks if user exists
     *
     * @param email Email à vérifier / Email to check
     * @return true si l'utilisateur existe / true if user exists
     */
    public boolean userExists(String email) {
        return userDatabase.containsKey(email);
    }

    /**
     * Enregistre une tentative de connexion pour audit
     * Records login attempt for audit
     *
     * @param attempt Tentative de connexion / Login attempt
     */
    public void recordLoginAttempt(LoginAttempt attempt) {
        loginHistory.add(attempt);

        // Garder seulement les 1000 dernières tentatives / Keep only last 1000 attempts
        if (loginHistory.size() > 1000) {
            loginHistory.remove(0);
        }

        String status = attempt.isSuccess() ? "SUCCÈS" : " ÉCHEC";
        log.info(" Tentative de connexion / Login attempt - {} - {} - {} - IP: {}",
                status, attempt.getEmail(), attempt.getProvider(), attempt.getIpAddress());
    }

    /**
     * Obtient l'adresse IP du client
     * Gets client IP address
     *
     * @return Adresse IP / IP address
     */
    public String getClientIpAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return "unknown";

        HttpServletRequest request = attributes.getRequest();
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress != null ? ipAddress : "unknown";
    }

    /**
     * Obtient l'agent utilisateur (navigateur)
     * Gets user agent (browser)
     *
     * @return User agent
     */
    public String getUserAgent() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return "unknown";

        return attributes.getRequest().getHeader("User-Agent");
    }

    /**
     * Récupère la liste des connexions récentes pour un utilisateur
     * Gets recent login history for a user
     *
     * @param email Email utilisateur / User email
     * @return Liste des tentatives / List of attempts
     */
    public List<LoginAttempt> getUserLoginHistory(String email) {
        return loginHistory.stream()
                .filter(attempt -> email.equals(attempt.getEmail()))
                .toList();
    }

    /**
     * Compte le nombre d'utilisateurs enregistrés
     * Counts registered users
     *
     * @return Nombre d'utilisateurs / Number of users
     */
    public long getUserCount() {
        return userDatabase.size();
    }
}