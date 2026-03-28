package kz.genvibe.media_management.service.internal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Organization;

public interface AuthService {
    void sendEmailVerification(String email);
    void sendEmailVerificationToStore(String email, Organization organization);
    AppUser verifyEmail(String token);
    void authenticate(AppUser appUser, HttpServletRequest request, HttpServletResponse response);
}
