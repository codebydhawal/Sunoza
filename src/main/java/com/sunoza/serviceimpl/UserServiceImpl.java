package com.sunoza.serviceimpl;

import com.sunoza.dtos.*;
import com.sunoza.model.*;
import com.sunoza.repository.*;
import com.sunoza.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository users; private final RoleRepository roles;
    public UserServiceImpl(UserRepository users, RoleRepository roles) { this.users=users; this.roles=roles; }
    @Override public UserResponse get(Long id) { return UserResponse.from(users.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"))); }
    @Override public List<UserResponse> all() { return users.findAll().stream().map(UserResponse::from).toList(); }
    @Override @Transactional public UserResponse update(Long id, UpdateUserRequest req) {
        User user=users.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        users.findByEmailIgnoreCase(req.email()).filter(existing -> !existing.getId().equals(id)).ifPresent(existing -> { throw new IllegalArgumentException("Email is already registered"); });
        user.setName(req.name().trim()); user.setEmail(req.email().trim().toLowerCase());
        return UserResponse.from(user);
    }
    @Override @Transactional public UserResponse setRoleAndStatus(Long id, String roleName, String statusName) {
        User user=users.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        Role role=roles.findByNameIgnoreCase(roleName).orElseThrow(() -> new IllegalArgumentException("Role must be USER or ADMIN"));
        try { user.setStatus(UserStatus.valueOf(statusName.toUpperCase())); }
        catch(IllegalArgumentException e) { throw new IllegalArgumentException("Status must be ACTIVE or INACTIVE"); }
        user.setRole(role);
        return UserResponse.from(user);
    }
    @Override @Transactional public void delete(Long id) { User user=users.findById(id).orElseThrow(() -> new NoSuchElementException("User not found")); user.setStatus(UserStatus.INACTIVE); }
}
