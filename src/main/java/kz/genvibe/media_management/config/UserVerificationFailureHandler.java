package kz.genvibe.media_management.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.genvibe.media_management.repository.AppUserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserVerificationFailureHandler implements AuthenticationFailureHandler {

    private final AppUserRepository appUserRepository;

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull AuthenticationException exception
    ) throws IOException {
        var email = request.getParameter("email");
        var user = appUserRepository.findByEmail(email);

        if (user.isPresent() && !user.get().isEmailVerified()) {
            response.sendRedirect("/auth/login?verifyEmail=" + user.get().getEmail());
            return;
        }

        response.sendRedirect("/auth/login?error");
    }

}

