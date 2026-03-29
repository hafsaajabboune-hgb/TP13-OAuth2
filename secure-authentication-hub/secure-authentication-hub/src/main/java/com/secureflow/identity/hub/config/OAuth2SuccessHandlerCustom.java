package com.secureflow.identity.hub.config;

import com.secureflow.identity.hub.model.UserIdentityDTO;
import com.secureflow.identity.hub.model.LoginAttempt;
import com.secureflow.identity.hub.service.UserProfileService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Gestionnaire personnalisé après une authentification OAuth2 réussie
 * Custom handler after successful OAuth2 authentication
 *
 * Cette classe est appelée automatiquement après qu'un utilisateur s'est
 * authentifié avec succès via Google (ou autre fournisseur OAuth2)
 * This class is automatically called after a user successfully authenticates
 * via Google (or another OAuth2 provider)
 *
 * @author SecureFlow Team
 * @version 1.0
 */
@Slf4j  // Lombok - Crée un logger automatiquement / Creates a logger automatically
@Component  // Déclare cette classe comme un bean Spring / Declares this class as a Spring bean
@RequiredArgsConstructor  // Lombok - Crée un constructeur avec tous les champs final / Creates constructor with all final fields
public class OAuth2SuccessHandlerCustom implements AuthenticationSuccessHandler {

    // ============================================================
    // INJECTION DES DÉPENDANCES / DEPENDENCY INJECTION
    // ============================================================

    private final UserProfileService userProfileService;  // Service pour gérer les profils utilisateurs / Service to manage user profiles

    // ============================================================
    // MÉTHODE PRINCIPALE - APPELÉE APRÈS AUTHENTIFICATION RÉUSSIE
    // MAIN METHOD - CALLED AFTER SUCCESSFUL AUTHENTICATION
    // ============================================================

    /**
     * Méthode appelée automatiquement après une authentification OAuth2 réussie
     * Method automatically called after successful OAuth2 authentication
     *
     * @param request La requête HTTP / The HTTP request
     * @param response La réponse HTTP / The HTTP response
     * @param authentication L'objet contenant les informations d'authentification / Object containing authentication information
     * @throws IOException En cas d'erreur d'entrée/sortie / In case of input/output error
     * @throws ServletException En cas d'erreur servlet / In case of servlet error
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // ============================================================
        // RÉCUPÉRATION DES INFORMATIONS OAuth2 / RETRIEVE OAuth2 INFORMATION
        // ============================================================

        // Cast de l'authentification vers OAuth2AuthenticationToken pour accéder aux données OAuth2
        // Cast authentication to OAuth2AuthenticationToken to access OAuth2 data
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        // Récupération de l'utilisateur OAuth2 (contient les attributs de Google)
        // Retrieve OAuth2 user (contains Google attributes)
        OAuth2User oauth2User = oauthToken.getPrincipal();

        // Récupération de tous les attributs de l'utilisateur (nom, email, photo, etc.)
        // Retrieve all user attributes (name, email, picture, etc.)
        Map<String, Object> userAttributes = oauth2User.getAttributes();

        // Récupération du nom du fournisseur (google, keycloak, etc.)
        // Retrieve provider name (google, keycloak, etc.)
        String providerName = oauthToken.getAuthorizedClientRegistrationId();

        // ============================================================
        // CRÉATION DU DTO UTILISATEUR AVEC LES DONNÉES OAuth2
        // CREATE USER DTO WITH OAuth2 DATA
        // ============================================================

        // Construction de l'objet UserIdentityDTO avec les données de Google
        // Building UserIdentityDTO object with Google data
        UserIdentityDTO userIdentity = UserIdentityDTO.builder()
                // Identifiant unique Google (sub = subject)
                // Unique Google identifier (sub = subject)
                .providerId(userAttributes.getOrDefault("sub", "").toString())

                // Nom du fournisseur d'authentification (google)
                // Authentication provider name (google)
                .providerName(providerName)

                // Adresse email de l'utilisateur
                // User email address
                .email(userAttributes.getOrDefault("email", "").toString())

                // Nom complet de l'utilisateur
                // User full name
                .fullName(userAttributes.getOrDefault("name", "").toString())

                // Prénom de l'utilisateur
                // User first name
                .firstName(userAttributes.getOrDefault("given_name", "").toString())

                // Nom de famille de l'utilisateur
                // User last name
                .lastName(userAttributes.getOrDefault("family_name", "").toString())

                // URL de la photo de profil
                // Profile picture URL
                .profilePicture(userAttributes.getOrDefault("picture", "").toString())

                // Langue préférée de l'utilisateur (défaut: fr)
                // User preferred language (default: fr)
                .locale(userAttributes.getOrDefault("locale", "fr").toString())

                // Date de naissance (si disponible)
                // Birth date (if available)
                .birthDate(userAttributes.getOrDefault("birthdate", "").toString())

                // Genre (si disponible)
                // Gender (if available)
                .gender(userAttributes.getOrDefault("gender", "").toString())

                // Indique si l'email a été vérifié par Google
                // Indicates if email has been verified by Google
                .emailVerified(Boolean.parseBoolean(userAttributes.getOrDefault("email_verified", "true").toString()))

                // Stockage des attributs bruts pour utilisation future
                // Store raw attributes for future use
                .rawAttributes(userAttributes)

                // Construction finale de l'objet
                // Final build of the object
                .build();

        // Mise à jour de la date et heure de la dernière connexion
        // Update last login date and time
        userIdentity.setLastLoginTime(LocalDateTime.now());

        // ============================================================
        // ENREGISTREMENT DU PROFIL UTILISATEUR
        // SAVE USER PROFILE
        // ============================================================

        // Sauvegarde ou mise à jour du profil utilisateur dans la base de données (simulée)
        // Save or update user profile in database (simulated)
        userProfileService.saveOrUpdateUserProfile(userIdentity);

        // ============================================================
        // ENREGISTREMENT DE LA TENTATIVE DE CONNEXION POUR AUDIT
        // RECORD LOGIN ATTEMPT FOR AUDIT
        // ============================================================

        // Création d'un enregistrement de connexion réussie
        // Create a successful login record
        LoginAttempt loginAttempt = LoginAttempt.success(
                userIdentity.getEmail(),           // Email de l'utilisateur / User email
                userIdentity.getProviderName(),    // Fournisseur utilisé / Provider used
                userProfileService.getClientIpAddress(),   // Adresse IP / IP address
                userProfileService.getUserAgent()           // Navigateur / Browser
        );

        // Enregistrement dans l'historique / Save to history
        userProfileService.recordLoginAttempt(loginAttempt);

        // ============================================================
        // SAUVEGARDE EN SESSION / SAVE IN SESSION
        // ============================================================

        // Stockage des informations utilisateur dans la session HTTP
        // Store user information in HTTP session
        request.getSession().setAttribute("userIdentity", userIdentity);

        // ============================================================
        // JOURNALISATION / LOGGING
        // ============================================================

        // Journalisation de la connexion réussie
        // Log successful login
        log.info("Connexion réussie / Successful login - Email: {}, Provider: {}, IP: {}",
                userIdentity.getEmail(),
                userIdentity.getProviderName(),
                userProfileService.getClientIpAddress()
        );

        // ============================================================
        // REDIRECTION BASÉE SUR LE PREMIÈRE CONNEXION
        // REDIRECTION BASED ON FIRST LOGIN
        // ============================================================

        // Vérification si l'utilisateur existe déjà (première connexion ou non)
        // Check if user already exists (first login or not)
        boolean isFirstLogin = !userProfileService.userExists(userIdentity.getEmail());

        if (isFirstLogin) {
            // Si c'est la première connexion, rediriger vers la page de bienvenue
            // If it's the first login, redirect to welcome page
            log.info("Nouvel utilisateur / New user - Redirection vers page de bienvenue / Redirecting to welcome page: {}",
                    userIdentity.getEmail());
            response.sendRedirect("/dashboard/welcome");
        } else {
            // Sinon, rediriger vers le tableau de bord principal
            // Otherwise, redirect to main dashboard
            log.info("Utilisateur existant / Existing user - Redirection vers tableau de bord / Redirecting to dashboard: {}",
                    userIdentity.getEmail());
            response.sendRedirect("/dashboard/home");
        }
    }
}