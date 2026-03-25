package kz.genvibe.media_management.model.domain.dto.jingle;

import jakarta.validation.constraints.*;
import kz.genvibe.media_management.model.entity.Jingle;
import kz.genvibe.media_management.model.enums.JingleCategory;
import kz.genvibe.media_management.model.enums.JingleRepeatingTime;
import kz.genvibe.media_management.model.enums.JingleVoice;

import java.time.LocalDate;

public record JingleCreateDto(
    @NotNull JingleVoice voice,
    @NotNull JingleCategory category,
    @NotNull @FutureOrPresent LocalDate startDate,
    @NotNull @Future LocalDate endDate,
    @NotNull JingleRepeatingTime repeatingTime,
    @NotBlank @Size(min = 1, max = 500) String announcementText
) {
    public Jingle toEntity() {
        return Jingle.builder()
            .voice(voice)
            .category(category)
            .startDate(startDate)
            .endDate(endDate)
            .duration(repeatingTime.getDuration())
            .announcementText(announcementText)
            .build();
    }
}
