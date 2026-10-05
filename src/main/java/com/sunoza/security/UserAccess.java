package com.sunoza.security;
import com.sunoza.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
@Component("userAccess")
public class UserAccess {
    private final UserRepository users;
    public UserAccess(UserRepository users) { this.users=users; }
    public boolean isSelf(Long id, Authentication authentication) { return authentication != null && users.findByEmailIgnoreCase(authentication.getName()).map(u -> u.getId().equals(id)).orElse(false); }
}
