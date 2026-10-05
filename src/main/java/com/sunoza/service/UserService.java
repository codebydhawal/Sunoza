package com.sunoza.service;
import com.sunoza.dtos.*;
import java.util.List;
public interface UserService {
    UserResponse get(Long id);
    List<UserResponse> all();
    UserResponse update(Long id, UpdateUserRequest request);
    UserResponse setRoleAndStatus(Long id, String role, String status);
    void delete(Long id);
}
