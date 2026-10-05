package com.sunoza.config;

import com.sunoza.model.Role;
import com.sunoza.repository.UserRepository;
import com.sunoza.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminBootstrap {
    @Bean CommandLineRunner seedRolesAndPromoteAdmin(UserRepository users, RoleRepository roles,
            @Value("${app.admin.email:}") String email) {
        return args -> {
            Role userRole=roles.findByNameIgnoreCase("USER").orElseGet(() -> roles.save(new Role("USER")));
            Role adminRole=roles.findByNameIgnoreCase("ADMIN").orElseGet(() -> roles.save(new Role("ADMIN")));
            users.findByRoleIsNull().forEach(user -> { user.setRole(userRole); users.save(user); });
            users.findByStatusIsNull().forEach(user -> { user.setStatus(com.sunoza.model.UserStatus.ACTIVE); users.save(user); });
            if (email.isBlank()) return;
            users.findByEmailIgnoreCase(email.trim()).ifPresent(user -> {
                user.setRole(adminRole);
                users.save(user);
            });
        };
    }
}
