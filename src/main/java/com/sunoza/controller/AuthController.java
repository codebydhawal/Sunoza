package com.sunoza.controller;

import com.sunoza.dtos.*;
import com.sunoza.repository.UserRepository;
import com.sunoza.service.AuthService;
import com.sunoza.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController @RequestMapping({"/rest/auth", "/api/auth"})
public class AuthController {
    private final AuthService auth; private final UserService userService; private final UserRepository users;
    public AuthController(AuthService auth, UserService userService, UserRepository users) { this.auth=auth; this.userService=userService; this.users=users; }
    @PostMapping("/register") public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201,"User registered successfully.",auth.register(request))); }
    @PostMapping("/login") public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) { return ResponseEntity.ok(ApiResponse.success(200,"Login successful.",auth.login(request))); }
    @PostMapping("/forgot-password") public ResponseEntity<ApiResponse<Map<String,String>>> forgot(@Valid @RequestBody ForgotPasswordRequest request) { auth.forgotPassword(request.email()); return ResponseEntity.ok(ApiResponse.success(200,"If the account exists, reset instructions will be sent.",Map.of("message","Request accepted"))); }
    @PostMapping("/reset-password") public ResponseEntity<ApiResponse<Map<String,String>>> reset(@Valid @RequestBody ResetPasswordRequest request) { auth.resetPassword(request); return ResponseEntity.ok(ApiResponse.success(200,"Password has been reset.",Map.of("message","Password has been reset"))); }

    @GetMapping("/me") public ResponseEntity<ApiResponse<UserResponse>> me(Authentication authn) { return ResponseEntity.ok(ApiResponse.success(200,"User fetched successfully.",users.findByEmailIgnoreCase(authn.getName()).map(UserResponse::from).orElseThrow())); }
    @GetMapping("/get") @PreAuthorize("hasRole('ADMIN') or @userAccess.isSelf(#p0, authentication)") public ResponseEntity<ApiResponse<UserResponse>> get(@RequestParam Long userId) { return ResponseEntity.ok(ApiResponse.success(200,"User fetched successfully.",userService.get(userId))); }
    @GetMapping("/get-all") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<List<UserResponse>>> all() { return ResponseEntity.ok(ApiResponse.success(200,"Users fetched successfully.",userService.all())); }
    @PutMapping("/update") @PreAuthorize("hasRole('ADMIN') or @userAccess.isSelf(#p0, authentication)") public ResponseEntity<ApiResponse<UserResponse>> update(@RequestParam Long userId, @Valid @RequestBody UpdateUserRequest request) { return ResponseEntity.ok(ApiResponse.success(200,"User updated successfully.",userService.update(userId,request))); }
    @DeleteMapping("/delete") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<Map<String,String>>> delete(@RequestParam Long userId) { userService.delete(userId); return ResponseEntity.ok(ApiResponse.success(200,"User deactivated successfully.",Map.of("status","INACTIVE"))); }
    @PatchMapping("/update-role-status") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<UserResponse>> updateRoleStatus(@RequestParam Long userId, @RequestParam String role, @RequestParam(defaultValue="ACTIVE") String status) { return ResponseEntity.ok(ApiResponse.success(200,"User role/status updated successfully.",userService.setRoleAndStatus(userId,role,status))); }
}
