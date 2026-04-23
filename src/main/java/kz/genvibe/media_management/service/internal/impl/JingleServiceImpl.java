package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.genvibe.media_management.exception.JingleCreationLimitExceededException;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleAddStoresDto;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleApproveDto;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Jingle;
import kz.genvibe.media_management.model.entity.JingleSchedule;
import kz.genvibe.media_management.model.entity.JingleSlot;
import kz.genvibe.media_management.model.enums.JingleSlotStatus;
import kz.genvibe.media_management.repository.JingleRepository;
import kz.genvibe.media_management.repository.JingleScheduleRepository;
import kz.genvibe.media_management.service.integration.ElevenlabsIntegrationService;
import kz.genvibe.media_management.service.internal.JingleService;
import kz.genvibe.media_management.service.internal.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JingleServiceImpl implements JingleService {

    private final StoreService storeService;
    private final ElevenlabsIntegrationService elevenlabsIntegrationService;
    private final JingleRepository jingleRepository;
    private final JingleScheduleRepository jingleScheduleRepository;

    @Override
    @Transactional
    public void createJingle(AppUser appUser, JingleCreateDto dto) {
        var organization = appUser.getOrganization();
        var jinglesCount = jingleRepository.countAllByOrganization(organization);

        if (jinglesCount >= 20) {
            throw new JingleCreationLimitExceededException("You exceeded the limit of 20");
        }

        var speechFileUrl = elevenlabsIntegrationService.getSpeechFileUrl(
            dto.announcementText(),
            dto.voice().getElevenlabsVoiceId()
        );

        var jingle = dto.toEntity();
        jingle.setOrganization(organization);
        jingle.setFileUrl(speechFileUrl);

        jingleRepository.save(jingle);

        var schedule = jingleScheduleRepository.findJingleScheduleByOrganization(organization)
            .orElseGet(() -> jingleScheduleRepository.save(JingleSchedule.builder().organization(organization).build()));

        generateSlotsForJingle(jingle, schedule);

        log.info("Jingle created for organization: {}", appUser.getOrganization().getCompanyName());
    }

    @Override
    @Transactional
    public void deleteJingleById(long id, AppUser appUser) {
        var jingle = jingleRepository.findJingleByIdAndOrganization(id, appUser.getOrganization())
            .orElseThrow(() -> new EntityNotFoundException("Jingle not found"));
        jingleRepository.delete(jingle);
    }

    @Override
    @Transactional
    public void setPauseApprovalStatus(
        long id,
        JingleApproveDto dto,
        AppUser appUser
    ) {
        var jingle = jingleRepository.findJingleByIdAndOrganization(id, appUser.getOrganization())
            .orElseThrow(() -> new EntityNotFoundException("Jingle not found"));
        jingle.setPauseApproved(dto.isApproved());
    }

    @Override
    @Transactional
    public void addJingleToStores(
        long id,
        JingleAddStoresDto dto,
        AppUser appUser
    ) {
        var jingle = jingleRepository.findJingleByIdAndOrganization(id, appUser.getOrganization())
            .orElseThrow(() -> new EntityNotFoundException("Jingle not found"));
        var stores = storeService.getAllStoresByAppUserAndNames(appUser, dto.storeNames());

        jingle.getStores().addAll(stores);
    }

    @Override
    @Transactional
    public void requestToPauseJingle(long id) {
        var jingle = jingleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Jingle not found"));
        jingle.setRequestedToPause(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Jingle> getJingleHistory(AppUser appUser) {
        return jingleRepository.findJinglesByOrganization(appUser.getOrganization());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Jingle> getJingleRequestsToPause(AppUser appUser) {
        return jingleRepository.findJinglesByOrganizationAndRequestedToPauseIsTrue(appUser.getOrganization());
    }

    private void generateSlotsForJingle(Jingle jingle, JingleSchedule schedule) {
        var now = LocalDateTime.now();

        if (now.isAfter(jingle.getEndDate()) || now.plusDays(1).isBefore(jingle.getStartDate())) {
            return;
        }

        var minutesInterval = jingle.getRepeatingTime().getDuration().toMinutes();
        var nextSlotTime = jingle.getStartDate().isAfter(now) ? jingle.getStartDate() : now;
        var endOfDay = now.toLocalDate().atTime(LocalTime.MAX);

        while (nextSlotTime.isBefore(endOfDay) && nextSlotTime.isBefore(jingle.getEndDate())) {
            var slot = JingleSlot.builder()
                .jingle(jingle)
                .jingleSchedule(schedule)
                .playTime(nextSlotTime)
                .status(JingleSlotStatus.PENDING)
                .build();

            schedule.addSlot(slot);
            nextSlotTime = nextSlotTime.plusMinutes(minutesInterval);
        }
    }

}
