package kz.genvibe.media_management.model.domain.dto.onboarding;

import jakarta.validation.constraints.NotNull;
import kz.genvibe.media_management.model.enums.SpacePurpose;

public record Step4Dto(
    @NotNull SpacePurpose spacePurpose
) { }
