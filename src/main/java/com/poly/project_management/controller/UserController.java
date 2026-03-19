package com.poly.project_management.controller;

import com.poly.project_management.dto.ApiResponse;
import com.poly.project_management.entity.UserEntity;
import com.poly.project_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserEntity>> registerUser(@RequestBody UserEntity user) {
        UserEntity savedUser = userService.register(user);
        ApiResponse<UserEntity> response = new ApiResponse<>(201, "Đăng ký thành công", savedUser);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> loginUser(@RequestBody com.poly.project_management.dto.LoginRequest loginRequest) {
        try {
            // Gọi Service để xử lý và lấy thẻ Token
            String token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

            // Gói gọn vào ApiResponse trả về
            ApiResponse<String> response = new ApiResponse<>(200, "Đăng nhập thành công!", token);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ApiResponse<String> response = new ApiResponse<>(400, e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }
}