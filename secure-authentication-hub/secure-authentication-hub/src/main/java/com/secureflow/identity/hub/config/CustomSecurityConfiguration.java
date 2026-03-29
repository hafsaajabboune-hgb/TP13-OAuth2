package com.secureflow.identity.hub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class CustomSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Configuration du handler de déconnexion
        SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        logoutSuccessHandler.setDefaultTargetUrl("/auth/login?logout=true");
        logoutSuccessHandler.setAlwaysUseDefaultTargetUrl(true);

        http
                // Désactiver CSRF pour simplifier
                .csrf(csrf -> csrf.disable())

                // Configuration des autorisations
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )

                // Configuration OAuth2 Login
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/dashboard/home", true)
                )

                // Configuration de la déconnexion
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}