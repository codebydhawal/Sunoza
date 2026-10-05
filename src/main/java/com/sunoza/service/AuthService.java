package com.sunoza.service;
import com.sunoza.dtos.*;
public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void forgotPassword(String email);
    void resetPassword(ResetPasswordRequest request);
}
