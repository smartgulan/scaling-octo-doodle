package kz.genvibe.media_management.model.domain.dto.user;

import kz.genvibe.media_management.model.enums.*;

import java.util.List;

public record AppUserUpdateDto(
    String password,
    String email,
    String companyName,
    BusinessType businessType,
    MusicProvider musicProvider,
    List<BrandIdentity> brandIdentity,
    List<CurrentFeel> currentFeel,
    SpacePurpose spacePurpose,
    PlaytimeWindow playtimeWindow
) { }
