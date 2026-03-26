package kz.genvibe.media_management.service.internal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.genvibe.media_management.model.domain.dto.user.PasswordSetupDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.domain.dto.user.AppUserUpdateDto;
import kz.genvibe.media_management.model.entity.MusicType;
import kz.genvibe.media_management.model.entity.Store;

import java.util.List;
import java.util.Set;

public interface UserService {
    // User modification methods
    AppUser setupUserPassword(
        PasswordSetupDto passwordSetupDto,
        HttpServletRequest request,
        HttpServletResponse response
    );
    void updateUser(AppUserUpdateDto dto, AppUser appUser);

    // User read methods
    Set<Store> getUserStores(String email);
    AppUser getUserByEmail(String email);

    // User entity fields modification methods
    void saveUserMusicTypes(AppUser appUser, List<String> musicTypeNames);
}
