package kz.genvibe.media_management.config;

import kz.genvibe.media_management.config.props.AppProps;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.enums.UserRole;
import kz.genvibe.media_management.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SystemUserInitializer {

    private final AppUserRepository appUserRepository;
    private final AppProps appProps;
    private final PasswordEncoder passwordEncoder;

//    @EventListener(ApplicationReadyEvent.class)
    public void createSystemUser() {
        var systemUserProperties = appProps.getSystemUser();

        if (appUserRepository.existsByEmail(systemUserProperties.email())) {
            log.debug("System user {} already exists, skipping initialization", systemUserProperties.email());
            return;
        }

        var systemUser = AppUser.builder()
            .email(systemUserProperties.email())
            .password(passwordEncoder.encode(systemUserProperties.password()))
            .role(UserRole.ROLE_ADMIN)
            .emailVerified(true)
            .build();

        appUserRepository.save(systemUser);
        log.info("System user '{}' created successfully", systemUserProperties.email());
    }

}
