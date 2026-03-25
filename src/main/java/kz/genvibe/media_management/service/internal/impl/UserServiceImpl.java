package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.genvibe.media_management.exception.UserAlreadyExistsException;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
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
        var email = passwordSetupDto.email();
        var appUser = appUserRepository.findByEmail(passwordSetupDto.email())
            .orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found"));

        appUser.setPassword(passwordEncoder.encode(passwordSetupDto.password()));

        var authentication = new UsernamePasswordAuthenticationToken(
            appUser.getEmail(),
            null,
            List.of(appUser.getRole())
        );
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        new HttpSessionSecurityContextRepository().saveContext(context, request, response);
    }

    @Override
    @Transactional
    public void updateUser(AppUserUpdateDto dto, AppUser appUser) {
        appUser.setFullName(dto.fullname());
        appUser.setCompanyRole(dto.companyRole());

        if (!dto.email().equals(appUser.getEmail())) {
            if (appUserRepository.existsByEmail(dto.email())) {
                throw new UserAlreadyExistsException("User with email: " + dto.email() + " already exists");
            }
            appUser.setEmail(dto.email());
            appUser.setEmailVerified(false);
        }

        appUserRepository.save(appUser);

        log.info("User updated with email: {}", appUser.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public MusicType getUserMusicType(String email) {
        var user = getUserForView(email);
        return user.getMusicType();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Store> getUserStores(String email) {
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
