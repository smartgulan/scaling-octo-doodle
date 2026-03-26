package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.genvibe.media_management.config.props.AppProps;
import kz.genvibe.media_management.exception.VerificationLinkExpiredException;
import kz.genvibe.media_management.model.domain.OnboardingSession;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.repository.AppUserRepository;
import kz.genvibe.media_management.service.internal.AuthService;
import kz.genvibe.media_management.service.internal.EmailVerificationTokenService;
import kz.genvibe.media_management.service.internal.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private static final String VERIFICATION_URL_PATH = "/auth/verify-email?token=";
    private static final String CONFIRMATION_URL_PATH = "/auth/confirm-email?token=";

    private final EmailVerificationTokenService emailVerificationTokenService;
    private final MailService mailService;
    private final AppUserRepository appUserRepository;
    private final AppProps appProps;
    private final OnboardingSession onboardingSession;
    private final TemplateEngine templateEngine;

    @Override
    @Transactional
    public void sendEmailVerification(String email) {
        var emailVerificationToken = emailVerificationTokenService.generate();

        AppUser appUser;
        String verificationLink;

        if (appUserRepository.existsByEmail(email)) {
            appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found for email verification"));
            if (appUser.isEmailVerified()) {
                throw new IllegalStateException("Email already verified");
            }
            verificationLink = appProps.getBaseUrl() + CONFIRMATION_URL_PATH + emailVerificationToken.getToken();
        } else {
            appUser = onboardingSession.toAppUser();
            appUser.setOnboardingCompleted(true);
            appUser.setEmail(email);
            verificationLink = appProps.getBaseUrl() + VERIFICATION_URL_PATH + emailVerificationToken.getToken();
        }

        appUser.setEmailVerificationToken(emailVerificationToken);
        appUserRepository.save(appUser);

        sendEmail(verificationLink, email);

        log.info("Sent email verification to {}", email);
    }

    @Override
    @Transactional
    public AppUser verifyEmail(String token) {
        var emailVerificationToken = emailVerificationTokenService.findByToken(token)
            .orElseThrow(() -> new EntityNotFoundException("Verification token not found: " + token));

        if (emailVerificationToken.isExpired()) {
            throw new VerificationLinkExpiredException();
        }

        var appUser = emailVerificationToken.getAppUser();
        appUser.setEmailVerified(true);
        appUser.setEmailVerificationToken(null);

        emailVerificationTokenService.delete(emailVerificationToken);

        log.info("Email verified for: {}", appUser.getEmail());

        return appUser;
    }

    @Override
    public void authenticate(AppUser appUser, HttpServletRequest request, HttpServletResponse response) {
        var authentication = new UsernamePasswordAuthenticationToken(
            appUser.getEmail(),
            null,
            List.of(appUser.getRole())
        );
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        new HttpSessionSecurityContextRepository().saveContext(context, request, response);

        log.info("Authenticated user with email: {}", appUser.getEmail());
    }

    private void sendEmail(String verificationUrl, String email) {
        var subject = "Email Address Verification";

        var context = new Context();
        context.setVariable("verificationUrl", verificationUrl);
        var html = templateEngine.process("pages/email/verify-email", context);

        mailService.sendHtmlMail(email, subject, html);
    }

}
