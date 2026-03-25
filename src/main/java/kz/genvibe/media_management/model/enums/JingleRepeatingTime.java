package kz.genvibe.media_management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public enum JingleRepeatingTime {
    EVERY_HOUR(Duration.ofHours(1)),
    EVERY_TWO_HOURS(Duration.ofHours(2)),
    EVERY_THREE_HOURS(Duration.ofHours(3)),
    EVERY_DAY(Duration.ofDays(1)),
    EVERY_WEEK(Duration.ofDays(7));

    private final Duration duration;
}
