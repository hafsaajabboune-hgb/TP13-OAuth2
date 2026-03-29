package com.secureflow.identity.hub.model;

import lombok.Getter;

/**
 * Énumération des fournisseurs d'authentification supportés
 * Enumeration of supported authentication providers
 *
 * Permet de gérer plusieurs fournisseurs OAuth2 facilement
 * Allows easy management of multiple OAuth2 providers
 */
@Getter
public enum AuthenticationProvider {

    GOOGLE("google", "Google Identity Platform", "https://accounts.google.com"),
    KEYCLOAK("keycloak", "Keycloak OpenID Provider", "http://localhost:8080/realms/ens-realm"),
    GITHUB("github", "GitHub OAuth", "https://github.com"),
    CUSTOM("custom", "Fournisseur personnalisé / Custom provider", null);

    private final String providerCode;     // Code unique du fournisseur / Unique provider code
    private final String displayName;      // Nom affiché / Display name
    private final String defaultIssuerUri; // URI par défaut / Default issuer URI

    AuthenticationProvider(String providerCode, String displayName, String defaultIssuerUri) {
        this.providerCode = providerCode;
        this.displayName = displayName;
        this.defaultIssuerUri = defaultIssuerUri;
    }

    /**
     * Trouve un fournisseur par son code
     * Finds a provider by its code
     *
     * @param code Code du fournisseur / Provider code
     * @return Fournisseur correspondant / Matching provider
     */
    public static AuthenticationProvider fromCode(String code) {
        for (AuthenticationProvider provider : values()) {
            if (provider.providerCode.equalsIgnoreCase(code)) {
                return provider;
            }
        }
        return CUSTOM;
    }

    /**
     * Vérifie si c'est le fournisseur Google
     * Checks if it's Google provider
     */
    public boolean isGoogle() {
        return this == GOOGLE;
    }

    /**
     * Vérifie si c'est Keycloak
     * Checks if it's Keycloak
     */
    public boolean isKeycloak() {
        return this == KEYCLOAK;
    }
}