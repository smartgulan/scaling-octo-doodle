package kz.genvibe.media_management.controller.auth;

import kz.genvibe.media_management.model.domain.OnboardingSession;
import kz.genvibe.media_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OnboardingSession onboardingSession;

    @GetMapping("/register")
    public String register() {
        return onboardingSession.getEmail() == null
            ? "redirect:/onboarding/welcome" : "pages/auth/register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "pages/auth/login";
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam String email) {
        onboardingSession.setEmail(email);
        authService.sendEmailVerification(email);
        return "redirect:/onboarding/verification-sent";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return "redirect:/auth/register";
    }

}
