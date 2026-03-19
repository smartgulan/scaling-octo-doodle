package kz.genvibe.media_management.service.internal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.genvibe.media_management.model.domain.dto.user.PasswordSetupDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.domain.dto.user.AppUserDto;
import kz.genvibe.media_management.model.domain.dto.user.AppUserUpdateDto;
import kz.genvibe.media_management.model.enums.UserRole;

public interface UserService {
    // User modification methods
    void createUser(
        PasswordSetupDto passwordSetupDto,
        HttpServletRequest request,
        HttpServletResponse response
    );
    void updateUser(AppUserUpdateDto dto, long id);
    void deleteUser(String email);

    // User read methods
    AppUser getUserByEmail(String email);

    // User state/role management methods
    void disableUser(String email);
    void updateUserRole(String email, UserRole newRole);
}
