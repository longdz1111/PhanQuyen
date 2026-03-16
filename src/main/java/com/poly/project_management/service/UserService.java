package com.poly.project_management.service;

import com.poly.project_management.entity.UserEntity;
import com.poly.project_management.enums.Role;
import com.poly.project_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity register(UserEntity user) {
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Lỗi: Tên không được để trống!");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Lỗi: Email không hợp lệ!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Lỗi: Email '" + user.getEmail() + "' đã được sử dụng!");
        }
        if (user.getRole() == null) {
            user.setRole(Role.MEMBER);
        }
        return userRepository.save(user);
    }
}