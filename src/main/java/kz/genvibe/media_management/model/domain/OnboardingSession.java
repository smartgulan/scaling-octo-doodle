package kz.genvibe.media_management.model.domain;

import kz.genvibe.media_management.model.entity.Organization;
import kz.genvibe.media_management.model.enums.*;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Component
@SessionScope
public class OnboardingSession implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String companyName;
    private BusinessType businessType;
    private MusicProvider musicProvider;
    private List<BrandIdentity> brandIdentities;
    private List<CurrentFeel> currentFeels;
    private SpacePurpose spacePurpose;
    private PlaytimeWindow playtimeWindow;

    public Organization toOrganization() {
        return Organization.builder()
            .companyName(companyName)
            .businessType(businessType)
            .musicProvider(musicProvider)
            .brandIdentity(brandIdentities)
            .currentFeel(currentFeels)
            .spacePurpose(spacePurpose)
            .playtimeWindow(playtimeWindow)
            .build();
    }
}
