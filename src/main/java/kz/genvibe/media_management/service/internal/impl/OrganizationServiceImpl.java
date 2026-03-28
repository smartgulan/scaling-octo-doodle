package kz.genvibe.media_management.service.internal.impl;

import kz.genvibe.media_management.model.domain.OnboardingSession;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Organization;
import kz.genvibe.media_management.repository.OrganizationRepository;
import kz.genvibe.media_management.service.internal.MusicService;
import kz.genvibe.media_management.service.internal.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    private final MusicService musicService;
    private final OrganizationRepository organizationRepository;
    private final OnboardingSession session;

    @Override
    @Transactional
    public Organization createOrganizationFromOnboarding(String email) {
        var organization = session.toOrganization();
        organization.setCreatedBy(email);

        organizationRepository.save(organization);

        log.info("Organization with company name: {} created by: {}", organization.getCompanyName(), email);

        return organization;
    }

    @Override
    @Transactional
    public void saveMusicTypes(AppUser appUser, List<String> musicTypeNames) {
        var organization = appUser.getOrganization();
        var musicTypes = musicService.getMusicTypesByNames(musicTypeNames);

        organization.setMusicTypes(musicTypes);

        organizationRepository.save(organization);
        log.info("Saved music types for organization: {}", organization.getCompanyName());
    }

}
