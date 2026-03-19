package kz.genvibe.media_management.model.domain.dto.onboarding;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import kz.genvibe.media_management.model.enums.BrandIdentity;

import java.util.List;

public record Step2Dto(
    @NotEmpty
    @Size(min = 2)
    List<BrandIdentity> brandIdentities
) { }
