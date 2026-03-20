package kz.genvibe.media_management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JingleVoice {
    RACHEL("Rachel", "Calm & Clear"),
    DOMI("Domi", "Warm & Friendly"),
    BELLA("Bella", "Soft & Natural"),
    ANTONI("Antoni", "Deep & Smooth");

    private final String name;
    private final String description;
}
