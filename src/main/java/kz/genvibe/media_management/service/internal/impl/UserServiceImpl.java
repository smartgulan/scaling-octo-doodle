package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.genvibe.media_management.model.domain.OnboardingSession;
import kz.genvibe.media_management.model.domain.dto.user.AppUserUpdateDto;
import kz.genvibe.media_management.model.domain.dto.user.PasswordSetupDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.MusicType;
import kz.genvibe.media_management.model.entity.Store;
import kz.genvibe.media_management.repository.AppUserRepository;
import kz.genvibe.media_management.service.internal.MusicService;
import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final MusicService musicService;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final OnboardingSession session;

    @Override
    @Transactional
    public void createUser(
        PasswordSetupDto passwordSetupDto,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        if (!session.isEmailVerified()) {
            throw new AccessDeniedException("Email not verified");
        }

        var appUser = AppUser.builder()
            .companyName(session.getCompanyName())
            .businessType(session.getBusinessType())
            .musicProvider(session.getMusicProvider())
            .brandIdentity(session.getBrandIdentities())
            .currentFeel(session.getCurrentFeels())
            .spacePurpose(session.getSpacePurpose())
            .playtimeWindow(session.getPlaytimeWindow())
            .onboardingCompleted(true)
            .email(session.getEmail())
            .password(passwordEncoder.encode(passwordSetupDto.password()))
            .emailVerified(true)
            .build();

        appUserRepository.save(appUser);

        var authentication = new UsernamePasswordAuthenticationToken(
            appUser.getEmail(),
            null,
            List.of(appUser.getRole())
        );
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        new HttpSessionSecurityContextRepository().saveContext(context, request, response);

        log.info("User created with email: {}", appUser.getEmail());
    }

    @Override
    public void updateUser(AppUserUpdateDto dto, long id) {

    }

    @Override
    public void deleteUser(String email) {

    }

    @Override
    @Transactional(readOnly = true)
    public AppUser getUserByEmail(String email) {
        return appUserRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public MusicType getUserMusicType(String email) {
        var user = getUserForView(email);
        return user.getMusicType();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Store> getUserStores(String email) {
        var user = getUserForView(email);
        return user.getStores();
    }

    @Override
    @Transactional
    public void saveUserMusicType(AppUser appUser, String musicTypeName) {
        var musicType = musicService.getMusicTypeByName(musicTypeName);
        appUser.setMusicType(musicType);

        appUserRepository.save(appUser);

        log.info("Saved music type for user: {}", appUser.getEmail());
    }

    @Transactional(readOnly = true)
    public AppUser getUserForView(String email) {
        return appUserRepository.findAppUserByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

}
