package com.secureflow.identity.hub.controller;

import com.secureflow.identity.hub.model.UserIdentityDTO;
import com.secureflow.identity.hub.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserProfileService userProfileService;
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH:mm");

    @GetMapping("/home")
    public String home(Model model, HttpSession session,
                       @AuthenticationPrincipal OAuth2User oauth2User) {

        UserIdentityDTO userIdentity = (UserIdentityDTO) session.getAttribute("userIdentity");

        if (userIdentity == null && oauth2User != null) {
            userIdentity = extractUserInfoFromOAuth2User(oauth2User);
            session.setAttribute("userIdentity", userIdentity);
        }

        if (userIdentity != null) {
            model.addAttribute("user", userIdentity);
            model.addAttribute("displayName", userIdentity.getDisplayName());
            model.addAttribute("initials", userIdentity.getInitials());
            model.addAttribute("hasProfilePicture", userIdentity.hasProfilePicture());
            model.addAttribute("profilePicture", userIdentity.getProfilePicture());
            model.addAttribute("currentTime", LocalDateTime.now().format(DATE_FORMATTER));

            log.info("Affichage dashboard pour: {}", userIdentity.getEmail());
        }

        return "dashboard/home";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        UserIdentityDTO userIdentity = (UserIdentityDTO) session.getAttribute("userIdentity");

        if (userIdentity != null) {
            model.addAttribute("user", userIdentity);
            model.addAttribute("displayName", userIdentity.getDisplayName());
            model.addAttribute("initials", userIdentity.getInitials());
            model.addAttribute("hasProfilePicture", userIdentity.hasProfilePicture());
            model.addAttribute("profilePicture", userIdentity.getProfilePicture());
            model.addAttribute("memberSince", userIdentity.getAccountCreatedAt().format(DATE_FORMATTER));
        }

        return "dashboard/profile";
    }

    private UserIdentityDTO extractUserInfoFromOAuth2User(OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();

        return UserIdentityDTO.builder()
                .providerId(attributes.getOrDefault("sub", "").toString())
                .providerName("google")
                .email(attributes.getOrDefault("email", "").toString())
                .fullName(attributes.getOrDefault("name", "").toString())
                .firstName(attributes.getOrDefault("given_name", "").toString())
                .lastName(attributes.getOrDefault("family_name", "").toString())
                .profilePicture(attributes.getOrDefault("picture", "").toString())
                .locale(attributes.getOrDefault("locale", "fr").toString())
                .emailVerified(Boolean.parseBoolean(attributes.getOrDefault("email_verified", "true").toString()))
                .rawAttributes(attributes)
                .build();
    }
}