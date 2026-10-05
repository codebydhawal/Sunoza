package com.sunoza.serviceimpl;

import com.sunoza.dtos.*;
import com.sunoza.model.*;
import com.sunoza.repository.*;
import com.sunoza.security.JwtService;
import com.sunoza.service.AuthService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.Instant;
import java.util.HexFormat;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordResetTokenRepository tokens;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;
    private final ObjectProvider<JavaMailSender> mailSender;
    private final String frontendUrl, mailFrom, adminEmail;

    public AuthServiceImpl(UserRepository users, RoleRepository roles, PasswordResetTokenRepository tokens, PasswordEncoder encoder, AuthenticationManager authManager, JwtService jwt, ObjectProvider<JavaMailSender> mailSender, @Value("${app.password-reset.frontend-url}") String frontendUrl, @Value("${app.mail.from}") String mailFrom, @Value("${app.admin.email:}") String adminEmail) {
        this.users = users;
        this.roles = roles;
        this.tokens = tokens;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwt = jwt;
        this.mailSender = mailSender;
        this.frontendUrl = frontendUrl;
        this.mailFrom = mailFrom;
        this.adminEmail = adminEmail;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (users.existsByEmailIgnoreCase(request.email()))
            throw new IllegalArgumentException("Email is already registered");
        String requestedRole = request.role().trim().toUpperCase();
        String email = request.email().trim().toLowerCase();
        if ("ADMIN".equals(requestedRole) && (adminEmail.isBlank() || !adminEmail.trim().equalsIgnoreCase(email)))
            throw new IllegalArgumentException("ADMIN registration is limited to the configured APP_ADMIN_EMAIL");
        Role role = roles.findByNameIgnoreCase(requestedRole).orElseThrow(() -> new IllegalStateException(requestedRole + " role was not initialized"));
        User user = users.save(new User(request.name().trim(), email, encoder.encode(request.password()), role));
        return response(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password()));
        return response(users.findByEmailIgnoreCase(request.email().trim().toLowerCase()).orElseThrow());
    }

    private AuthResponse response(User user) {
        return new AuthResponse(jwt.generate(user), "Bearer", jwt.getExpirationMs() / 1000, UserResponse.from(user));
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        users.findByEmailIgnoreCase(email.trim().toLowerCase()).ifPresent(user -> {
            String token = randomToken();
            tokens.deleteByUserId(user.getId());
            tokens.save(new PasswordResetToken(user, hash(token), Instant.now().plusSeconds(1800)));
            JavaMailSender sender = mailSender.getIfAvailable();
            if (sender != null) {
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setFrom(mailFrom);
                mail.setTo(user.getEmail());
                mail.setSubject("Reset your Sunoza password");
                mail.setText("Use this link within 30 minutes to reset your password: " + frontendUrl + "?token=" + token);
                sender.send(mail);
            }
        });
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken reset = tokens.findByTokenHash(hash(request.token())).filter(t -> t.getExpiresAt().isAfter(Instant.now())).orElseThrow(() -> new IllegalArgumentException("Reset token is invalid or expired"));
        reset.getUser().setPassword(encoder.encode(request.newPassword()));
        tokens.delete(reset);
    }

    private String randomToken() {
        byte[] b = new byte[32];
        new SecureRandom().nextBytes(b);
        return HexFormat.of().formatHex(b);
    }

    private String hash(String token) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
