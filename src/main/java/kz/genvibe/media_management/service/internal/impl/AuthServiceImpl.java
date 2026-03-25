package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.genvibe.media_management.config.props.AppProps;
import kz.genvibe.media_management.exception.VerificationLinkExpiredException;
import kz.genvibe.media_management.model.domain.OnboardingSession;
import kz.genvibe.media_management.model.entity.EmailVerificationToken;
import kz.genvibe.media_management.repository.AppUserRepository;
import kz.genvibe.media_management.repository.EmailVerificationTokenRepository;
import kz.genvibe.media_management.service.internal.AuthService;
import kz.genvibe.media_management.service.internal.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private static final String VERIFICATION_URL_PATH = "/auth/verify-email?token=";

    private final MailService mailService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final AppProps appProps;
    private final OnboardingSession onboardingSession;
    private final AppUserRepository appUserRepository;
    private final TemplateEngine templateEngine;

    @Override
    @Transactional
    public void sendEmailVerification(String email) {
        var token = UUID.randomUUID().toString();
        var expiry = Instant.now().plus(appProps.getVerificationToken().expiration());
        var emailVerificationToken = new EmailVerificationToken(token, expiry);

        emailVerificationTokenRepository.save(emailVerificationToken);

        var appUser = onboardingSession.toAppUser();
        appUser.setOnboardingCompleted(true);
        appUser.setEmail(email);
        appUser.setEmailVerificationToken(emailVerificationToken);

        appUserRepository.save(appUser);

        var verificationUrl = appProps.getBaseUrl() + VERIFICATION_URL_PATH + token;

        var subject = "Email Address Verification";
        var text = "To verify your email press the link: " + verificationUrl;

        var context = new Context();
        context.setVariable("verificationUrl", verificationUrl);
        var html = templateEngine.process("pages/email/verify-email", context);

        mailService.sendHtmlMail(email, subject, html);
        log.info("Sent email verification to {}", email);
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        var emailVerificationToken = emailVerificationTokenRepository.findByToken(token)
            .orElseThrow(() -> new EntityNotFoundException("Verification token not found: " + token));

        if (emailVerificationToken.isExpired()) {
            throw new VerificationLinkExpiredException();
        }

        var appUser = emailVerificationToken.getAppUser();
        appUser.setEmailVerified(true);
        appUser.setEmailVerificationToken(null);

        emailVerificationTokenRepository.delete(emailVerificationToken);

        log.info("Email verified for: {}", appUser.getEmail());
    }

}
