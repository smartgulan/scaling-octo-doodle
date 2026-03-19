package kz.genvibe.media_management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BrandIdentity {
    WARM_AND_WELCOMING("Warm & Welcoming"),
    MODERN_AND_ENERGETIC("Modern & Energetic"),
    PREMIUM_AND_SOPHISTICATED("Premium & Sophisticated"),
    SOCIAL_AND_STYLISH("Social & Stylish"),
    CREATIVE_AND_PLAYFUL("Creative & Playful"),
    NATURAL_AND_MINDFUL("Natural & Mindful"),
    TRADITIONAL_AND_CULTURAL("Traditional & Cultural");

    private final String name;
}
