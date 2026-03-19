package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import kz.genvibe.media_management.exception.UserAlreadyExistsException;
import kz.genvibe.media_management.model.domain.OnboardingSession;
import kz.genvibe.media_management.model.domain.dto.user.AppUserDto;
import kz.genvibe.media_management.model.domain.dto.user.AppUserUpdateDto;
import kz.genvibe.media_management.model.domain.dto.user.PasswordSetupDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.enums.UserRole;
import kz.genvibe.media_management.repository.AppUserRepository;
import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final OnboardingSession session;

    @Override
    @Transactional
    public void createUser(PasswordSetupDto passwordSetupDto) {
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
    }

    @Override
    public void updateUser(AppUserUpdateDto dto, long id) {

    }

    @Override
    public void deleteUser(String email) {

    }

    @Override
    public AppUser getUserByEmail(String email) {
        return null;
    }

    @Override
    public void disableUser(String email) {

    }

    @Override
    public void updateUserRole(String email, UserRole newRole) {

    }

}
