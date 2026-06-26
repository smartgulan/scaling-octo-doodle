package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.genvibe.media_management.exception.JingleCreationLimitExceededException;
import kz.genvibe.media_management.model.domain.PlayerCommand;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleCreateDto;
import kz.genvibe.media_management.model.entity.*;
import kz.genvibe.media_management.model.enums.CommandType;
import kz.genvibe.media_management.model.enums.JingleSlotStatus;
import kz.genvibe.media_management.repository.JingleRepository;
import kz.genvibe.media_management.repository.JingleSlotRepository;
import kz.genvibe.media_management.service.integration.ElevenlabsIntegrationService;
import kz.genvibe.media_management.service.internal.JingleService;
import kz.genvibe.media_management.service.internal.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JingleServiceImpl implements JingleService {

    private final StoreService storeService;
    private final ElevenlabsIntegrationService elevenlabsIntegrationService;
    private final JingleRepository jingleRepository;
    private final JingleSlotRepository jingleSlotRepository;
    private final SimpMessagingTemplate messagingTemplate;

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

        log.info("Jingle created for organization: {}", appUser.getOrganization().getCompanyName());
    }

    @Override
    @Transactional
    public void deleteJingleById(long id, AppUser appUser) {
        var jingle = jingleRepository.findJingleByIdAndOrganization(id, appUser.getOrganization())
                .orElseThrow(() -> new EntityNotFoundException("Jingle not found"));
        jingleRepository.deleteByIdAndOrg(id, appUser.getOrganization());
    }

    @Override
    @Transactional
    public void setPauseApprovalStatus(
        long id,
        AppUser appUser
    ) {
        var jingle = jingleRepository.findJingleByIdAndOrganization(id, appUser.getOrganization())
            .orElseThrow(() -> new EntityNotFoundException("Jingle not found"));
        jingle.setPauseApproved(true);
    }

    @Override
    @Transactional
    public void addJingleToStores(
        long id,
        List<Long> idList,
        AppUser appUser
    ) {
        var jingle = jingleRepository.findJingleByIdAndOrganization(id, appUser.getOrganization())
            .orElseThrow(() -> new EntityNotFoundException("Jingle not found"));
        var stores = storeService.getAllStoresByAppUserAndIdList(appUser, idList);

        jingle.getStores().addAll(stores);

        final var jingleSchedules = stores.stream()
            .map(Store::getJingleSchedule)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        var today = LocalDate.now();
        var now = LocalDateTime.now().withSecond(0).withNano(0);

        for (final var schedule : jingleSchedules) {
            generateSlotsForJingleOnDay(jingle, schedule, today, now);
        }
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

    /**
     * Re-materializes today's slots for every active, store-assigned jingle.
     * Runs hourly so that multi-day jingles keep getting slots after the date rolls
     * over and so a jingle that starts later today is picked up even if the app was
     * restarted. Generation is idempotent (duplicates are skipped) and never creates
     * slots in the past, so running it often is safe.
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void generateDailySlots() {
        var today = LocalDate.now();
        var now = LocalDateTime.now().withSecond(0).withNano(0);
        var startOfDay = today.atStartOfDay();
        var endOfDay = today.atTime(LocalTime.MAX);

        var jingles = jingleRepository.findActiveAssignedJingles(startOfDay, endOfDay);

        for (final var jingle : jingles) {
            for (final var store : jingle.getStores()) {
                var schedule = store.getJingleSchedule();
                if (schedule == null) {
                    continue;
                }
                generateSlotsForJingleOnDay(jingle, schedule, today, now);
            }
        }

        log.info("Daily slot generation finished for {} active jingle(s)", jingles.size());
    }

    /**
     * Generates the PENDING slots of {@code jingle} that fall on {@code day} for a single
     * schedule. Slot times follow the jingle's repeating cadence anchored at its start date.
     * Slots earlier than {@code notBefore} are skipped (so we never schedule the past), and
     * times that already have a slot for this jingle in this schedule are skipped (idempotent).
     */
    private void generateSlotsForJingleOnDay(
        Jingle jingle,
        JingleSchedule schedule,
        LocalDate day,
        LocalDateTime notBefore
    ) {
        var startOfDay = day.atStartOfDay();
        var endOfDay = day.atTime(LocalTime.MAX);

        // Jingle is not active on this day at all.
        if (jingle.getStartDate().isAfter(endOfDay) || jingle.getEndDate().isBefore(startOfDay)) {
            return;
        }

        var interval = jingle.getRepeatingTime().getDuration().toMinutes();

        var existingTimes = schedule.getDailyJingleSlots().stream()
            .filter(slot -> Objects.equals(slot.getJingle().getId(), jingle.getId()))
            .map(JingleSlot::getPlayTime)
            .collect(Collectors.toSet());

        // First cadence point (anchored at the jingle start) that lands within this day.
        var slotTime = jingle.getStartDate();
        if (slotTime.isBefore(startOfDay)) {
            var steps = Duration.between(slotTime, startOfDay).toMinutes() / interval;
            slotTime = slotTime.plusMinutes(steps * interval);
            while (slotTime.isBefore(startOfDay)) {
                slotTime = slotTime.plusMinutes(interval);
            }
        }

        while (!slotTime.isAfter(endOfDay) && !slotTime.isAfter(jingle.getEndDate())) {
            if (!slotTime.isBefore(notBefore) && !existingTimes.contains(slotTime)) {
                schedule.addSlot(JingleSlot.builder()
                    .jingle(jingle)
                    .jingleSchedule(schedule)
                    .playTime(slotTime)
                    .status(JingleSlotStatus.PENDING)
                    .build());
            }
            slotTime = slotTime.plusMinutes(interval);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void checkAndBroadcastJingles() {
        log.info("Starting broadcast");
        var now = LocalDateTime.now().withSecond(0).withNano(0);
        var currentSlots = jingleSlotRepository.findJingleSlotsByPlayTimeLessThanEqualAndStatusAndJingleRequestedToPauseIsFalse(
            now,
            JingleSlotStatus.PENDING
        );

        for (var slot : currentSlots) {
            var storeId = slot.getJingleSchedule().getStore().getId();

            var command = new PlayerCommand(
                CommandType.PLAY_JINGLE,
                slot.getJingle().getFileUrl(),
                slot.getId(),
                slot.getJingle().getSpeed()
            );

            messagingTemplate.convertAndSend("/topic/store." + storeId + ".commands", command);

            slot.setStatus(JingleSlotStatus.PLAYED);
        }

        if (!currentSlots.isEmpty()) {
            log.info("Broadcasted {} jingles at {}", currentSlots.size(), now);
        }
    }

}
