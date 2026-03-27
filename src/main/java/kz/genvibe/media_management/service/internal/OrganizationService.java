package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Organization;

import java.util.List;

public interface OrganizationService {
    Organization createOrganizationFromOnboarding(String email);
    void saveMusicTypes(AppUser appUser, List<String> musicTypeNames);
}
