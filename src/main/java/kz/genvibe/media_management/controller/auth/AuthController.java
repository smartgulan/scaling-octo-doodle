package kz.genvibe.media_management.controller.auth;

import kz.genvibe.media_management.service.internal.AuthService;
import kz.genvibe.media_management.service.internal.UserService;
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
    private final UserService userService;

    @GetMapping("/register")
    public String register() {
        return "pages/auth/register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "pages/auth/login";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return "redirect:/auth/register";
    }

    @GetMapping("/confirm-email")
    public String confirmEmail() {
        return "pages/auth/confirm-email";
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam String email) {
        authService.sendEmailVerification(email);
        var appUser = userService.getUserByEmail(email);

        return appUser.isEmailChanged()
            ? "redirect:/auth/confirm-email" : "redirect:/onboarding/verification-sent";
    }

}
