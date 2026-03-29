package com.secureflow.identity.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import jakarta.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.logging.Logger;

/**
 * Point d'entrée principal de l'application
 * Main application entry point
 *
 * Cette application démontre l'intégration OAuth2 avec Google
 * This application demonstrates OAuth2 integration with Google
 *
 * @author SecureFlow Team
 * @version 2.0.0
 */
@SpringBootApplication
@EnableScheduling  // Active les tâches planifiées / Enables scheduled tasks
public class SecureAuthenticationHubApplication {

	private static final Logger logger = Logger.getLogger(SecureAuthenticationHubApplication.class.getName());

	/**
	 * Méthode principale - Point de démarrage
	 * Main method - Starting point
	 */
	public static void main(String[] args) {
		SpringApplication.run(SecureAuthenticationHubApplication.class, args);
		logger.info(" Application SecureAuthenticationHub démarrée avec succès !");
		logger.info(" SecureAuthenticationHub application started successfully!");
		logger.info(" Accédez à l'application : http://localhost:8088");
		logger.info(" Access the application at: http://localhost:8088");
	}

	/**
	 * Configuration du fuseau horaire par défaut
	 * Default timezone configuration
	 * Exécuté au démarrage / Executed at startup
	 */
	@PostConstruct
	public void init() {
		// Configuration du fuseau horaire pour toute l'application
		// Setting timezone for the whole application
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Europe/Paris")));
		logger.info(" Fuseau horaire configuré : Europe/Paris");
		logger.info(" Timezone configured: Europe/Paris");
	}
}