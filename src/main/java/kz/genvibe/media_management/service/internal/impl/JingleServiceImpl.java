package kz.genvibe.media_management.service.internal.impl;

import kz.genvibe.media_management.model.domain.dto.jingle.JingleCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Jingle;
import kz.genvibe.media_management.repository.JingleRepository;
import kz.genvibe.media_management.service.integration.ElevenlabsIntegrationService;
import kz.genvibe.media_management.service.internal.JingleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JingleServiceImpl implements JingleService {

    private final ElevenlabsIntegrationService elevenlabsIntegrationService;
    private final JingleRepository jingleRepository;

    @Override
    @Transactional
    public void createJingle(AppUser appUser, JingleCreateDto dto) {
        var speechFileUrl = elevenlabsIntegrationService.getSpeechFileUrl(
            dto.announcementText(),
            dto.voice().getElevenlabsVoiceId()
        );

        var jingle = dto.toEntity();
        jingle.setAppUser(appUser);
        jingle.setFileUrl(speechFileUrl);

        jingleRepository.save(jingle);

        log.info("Jingle created");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Jingle> getJingleHistory(AppUser appUser) {
        return jingleRepository.findJinglesByAppUser(appUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Jingle> getJingleRequestsToPause(AppUser appUser) {
        return jingleRepository.findJinglesByAppUserAndRequestedToPauseIsTrue(appUser);
    }

}
