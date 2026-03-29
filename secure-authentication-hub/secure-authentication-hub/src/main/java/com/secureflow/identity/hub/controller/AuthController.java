package com.secureflow.identity.hub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class AuthController {

    @GetMapping("/")
    public String root() {
        log.info("Redirection de la racine vers la page de connexion");
        return "redirect:/auth/login";
    }

    @GetMapping("/auth/login")
    public String login(Model model,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        HttpServletRequest request) {

        if (error != null) {
            model.addAttribute("errorMessage", "Échec de l'authentification. Veuillez réessayer.");
            model.addAttribute("errorDetails", "Authentication failed. Please try again.");
            log.warn("Tentative d'authentification échouée");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "Vous avez été déconnecté avec succès !");
            model.addAttribute("successDetails", "You have been successfully logged out!");
            log.info("Utilisateur déconnecté avec succès");
        }

        model.addAttribute("googleAuthUrl", "/oauth2/authorization/google");

        log.info("Page de connexion affichée");

        return "auth/login";
    }

    @GetMapping("/auth/logout")
    public void handleLogout(HttpServletResponse response) throws IOException {
        log.info("Redirection vers /logout");
        response.sendRedirect("/logout");
    }
}