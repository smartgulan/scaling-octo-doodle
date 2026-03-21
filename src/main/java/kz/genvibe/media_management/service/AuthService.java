package kz.genvibe.media_management.service;

public interface AuthService {
    void sendEmailVerification(String email);
    void verifyEmail(String token);
}
