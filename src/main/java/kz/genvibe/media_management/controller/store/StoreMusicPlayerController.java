package kz.genvibe.media_management.controller.store;

import jakarta.persistence.EntityNotFoundException;
import kz.genvibe.media_management.model.domain.PlayerInitialState;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleSlotDto;
import kz.genvibe.media_management.model.entity.Music;
import kz.genvibe.media_management.model.enums.JingleSlotStatus;
import kz.genvibe.media_management.repository.JingleScheduleRepository;
import kz.genvibe.media_management.repository.MusicRepository;
import kz.genvibe.media_management.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StoreMusicPlayerController {

    private final OrganizationRepository organizationRepository;
    private final JingleScheduleRepository scheduleRepository;
    private final MusicRepository musicRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sync.{orgId}")
    public void syncSchedule(@DestinationVariable long orgId) {
        var org = organizationRepository.findById(orgId)
            .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        var musicUrls = musicRepository
            .findAllByAtmosphereAndMood(org.getMusicAtmosphere(), org.getMusicMood())
            .stream()
            .map(Music::getFileUrl)
            .toList();

        var schedule = scheduleRepository.findJingleScheduleByOrganization(org)
            .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        var pendingSlots = schedule.getDailyJingleSlots().stream()
            .filter(slot -> slot.getStatus() == JingleSlotStatus.PENDING)
            .map(slot -> new JingleSlotDto(slot.getId(), slot.getPlayTime(), slot.getJingle().getFileUrl()))
            .toList();

        var initialState = new PlayerInitialState(
            musicUrls,
            pendingSlots,
            org.getCompanyName()
        );

        messagingTemplate.convertAndSend("/topic/org." + orgId + ".init", initialState);
    }

}
