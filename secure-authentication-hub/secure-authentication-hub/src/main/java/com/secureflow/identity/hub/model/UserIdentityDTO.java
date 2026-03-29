package com.secureflow.identity.hub.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Objet de transfert pour les données d'identité utilisateur
 * Data Transfer Object for user identity information
 *
 * Cette classe encapsule toutes les informations récupérées du fournisseur OAuth2
 * This class encapsulates all information retrieved from the OAuth2 provider
 *
 * @author SecureFlow Team
 * @version 2.0
 */
public class UserIdentityDTO {

    // ========== CHAMPS / FIELDS ==========

    private String providerId;           // ID unique chez le fournisseur / Unique provider ID
    private String providerName;         // Nom du fournisseur (google, keycloak) / Provider name
    private String registrationId;       // ID d'enregistrement OAuth2 / OAuth2 registration ID
    private String email;                // Adresse email / Email address
    private String fullName;             // Nom complet / Full name
    private String firstName;            // Prénom / First name
    private String lastName;             // Nom de famille / Last name
    private String profilePicture;       // URL photo de profil / Profile picture URL
    private String locale;               // Langue préférée / Preferred language
    private String gender;               // Genre (si disponible) / Gender (if available)
    private String birthDate;            // Date de naissance / Birth date
    private LocalDateTime lastLoginTime;     // Dernière connexion / Last login time
    private LocalDateTime accountCreatedAt;  // Date création compte / Account creation date
    private boolean emailVerified;           // Email vérifié ? / Email verified?
    private String preferredUsername;        // Nom d'utilisateur préféré / Preferred username
    private Map<String, Object> rawAttributes;  // Attributs bruts du fournisseur / Raw provider attributes
    private Map<String, String> customClaims;   // Claims personnalisés / Custom claims

    // ========== CONSTRUCTEURS / CONSTRUCTORS ==========

    /**
     * Constructeur par défaut avec initialisation
     * Default constructor with initialization
     */
    public UserIdentityDTO() {
        this.accountCreatedAt = LocalDateTime.now();
        this.emailVerified = true;
        this.rawAttributes = new HashMap<>();
        this.customClaims = new HashMap<>();
    }

    /**
     * Constructeur avec tous les paramètres
     * Constructor with all parameters
     */
    public UserIdentityDTO(String providerId, String providerName, String registrationId,
                           String email, String fullName, String firstName, String lastName,
                           String profilePicture, String locale, String gender, String birthDate,
                           LocalDateTime lastLoginTime, LocalDateTime accountCreatedAt,
                           boolean emailVerified, String preferredUsername,
                           Map<String, Object> rawAttributes, Map<String, String> customClaims) {
        this.providerId = providerId;
        this.providerName = providerName;
        this.registrationId = registrationId;
        this.email = email;
        this.fullName = fullName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.locale = locale;
        this.gender = gender;
        this.birthDate = birthDate;
        this.lastLoginTime = lastLoginTime;
        this.accountCreatedAt = accountCreatedAt != null ? accountCreatedAt : LocalDateTime.now();
        this.emailVerified = emailVerified;
        this.preferredUsername = preferredUsername;
        this.rawAttributes = rawAttributes != null ? rawAttributes : new HashMap<>();
        this.customClaims = customClaims != null ? customClaims : new HashMap<>();
    }

    // ========== GETTERS ET SETTERS ==========

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public LocalDateTime getAccountCreatedAt() {
        return accountCreatedAt;
    }

    public void setAccountCreatedAt(LocalDateTime accountCreatedAt) {
        this.accountCreatedAt = accountCreatedAt;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public Map<String, Object> getRawAttributes() {
        return rawAttributes;
    }

    public void setRawAttributes(Map<String, Object> rawAttributes) {
        this.rawAttributes = rawAttributes;
    }

    public Map<String, String> getCustomClaims() {
        return customClaims;
    }

    public void setCustomClaims(Map<String, String> customClaims) {
        this.customClaims = customClaims;
    }

    // ========== MÉTHODES UTILITAIRES / UTILITY METHODS ==========

    /**
     * Obtient le nom d'affichage formaté
     * Gets formatted display name
     *
     * @return Nom formaté pour l'affichage / Formatted name for display
     */
    public String getDisplayName() {
        if (firstName != null && !firstName.isEmpty()) {
            String lastNamePart = (lastName != null && !lastName.isEmpty()) ? " " + lastName : "";
            return firstName + lastNamePart;
        }
        return fullName != null ? fullName : "Cher utilisateur / Dear user";
    }

    /**
     * Vérifie si l'utilisateur a une photo de profil
     * Checks if user has a profile picture
     *
     * @return true si photo disponible / true if picture available
     */
    public boolean hasProfilePicture() {
        return profilePicture != null && !profilePicture.isEmpty();
    }

    /**
     * Obtient les initiales de l'utilisateur pour un avatar par défaut
     * Gets user initials for default avatar
     *
     * @return Initiales / Initials
     */
    public String getInitials() {
        if (firstName != null && !firstName.isEmpty()) {
            char firstInitial = firstName.charAt(0);
            if (lastName != null && !lastName.isEmpty()) {
                return String.valueOf(firstInitial) + lastName.charAt(0);
            }
            return String.valueOf(firstInitial);
        }
        return "U";
    }

    /**
     * Retourne une représentation textuelle des informations principales
     * Returns textual representation of main information
     */
    @Override
    public String toString() {
        return String.format("UserIdentityDTO{provider='%s', email='%s', name='%s'}",
                providerName, email, getDisplayName());
    }

    // ========== BUILDER PATTERN ==========

    /**
     * Crée un nouveau Builder pour UserIdentityDTO
     * Creates a new Builder for UserIdentityDTO
     *
     * @return Une instance du Builder / A Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder pattern pour créer UserIdentityDTO de manière fluide
     * Builder pattern to create UserIdentityDTO in a fluent way
     */
    public static class Builder {
        private String providerId;
        private String providerName;
        private String registrationId;
        private String email;
        private String fullName;
        private String firstName;
        private String lastName;
        private String profilePicture;
        private String locale;
        private String gender;
        private String birthDate;
        private LocalDateTime lastLoginTime;
        private LocalDateTime accountCreatedAt;
        private boolean emailVerified = true;  // Valeur par défaut / Default value
        private String preferredUsername;
        private Map<String, Object> rawAttributes;
        private Map<String, String> customClaims;

        public Builder providerId(String providerId) {
            this.providerId = providerId;
            return this;
        }

        public Builder providerName(String providerName) {
            this.providerName = providerName;
            return this;
        }

        public Builder registrationId(String registrationId) {
            this.registrationId = registrationId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder profilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
            return this;
        }

        public Builder locale(String locale) {
            this.locale = locale;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder birthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder lastLoginTime(LocalDateTime lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
            return this;
        }

        public Builder accountCreatedAt(LocalDateTime accountCreatedAt) {
            this.accountCreatedAt = accountCreatedAt;
            return this;
        }

        public Builder emailVerified(boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }

        public Builder preferredUsername(String preferredUsername) {
            this.preferredUsername = preferredUsername;
            return this;
        }

        public Builder rawAttributes(Map<String, Object> rawAttributes) {
            this.rawAttributes = rawAttributes;
            return this;
        }

        public Builder customClaims(Map<String, String> customClaims) {
            this.customClaims = customClaims;
            return this;
        }

        /**
         * Construit l'objet UserIdentityDTO avec les valeurs configurées
         * Builds the UserIdentityDTO object with the configured values
         *
         * @return L'objet UserIdentityDTO construit / The built UserIdentityDTO object
         */
        public UserIdentityDTO build() {
            UserIdentityDTO dto = new UserIdentityDTO();
            dto.providerId = this.providerId;
            dto.providerName = this.providerName;
            dto.registrationId = this.registrationId;
            dto.email = this.email;
            dto.fullName = this.fullName;
            dto.firstName = this.firstName;
            dto.lastName = this.lastName;
            dto.profilePicture = this.profilePicture;
            dto.locale = this.locale;
            dto.gender = this.gender;
            dto.birthDate = this.birthDate;
            dto.lastLoginTime = this.lastLoginTime;
            dto.accountCreatedAt = this.accountCreatedAt != null ? this.accountCreatedAt : LocalDateTime.now();
            dto.emailVerified = this.emailVerified;
            dto.preferredUsername = this.preferredUsername;
            dto.rawAttributes = this.rawAttributes != null ? this.rawAttributes : new HashMap<>();
            dto.customClaims = this.customClaims != null ? this.customClaims : new HashMap<>();
            return dto;
        }
    }
}