package com.sunoza.dtos;
import com.sunoza.model.User;
import java.time.Instant;
public record UserResponse(Long id, String name, String email, String role, String status, Instant createdAt) {
    public static UserResponse from(User u) { return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole().getName(), u.getStatus().name(), u.getCreatedAt()); }
}
