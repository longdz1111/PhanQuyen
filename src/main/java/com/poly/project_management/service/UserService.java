package com.poly.project_management.service;

import com.poly.project_management.entity.RoleEntity;
import com.poly.project_management.entity.UserEntity;
import com.poly.project_management.repository.RoleRepository;
import com.poly.project_management.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        RoleEntity userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalArgumentException("Lỗi: Hệ thống chưa có quyền USER!"));
        user.getRoles().add(userRole);

        return userRepository.save(user);
    }

    @Autowired
    private com.poly.project_management.until.JwtUtil jwtUtil;
    // HÀM ĐĂNG NHẬP MỚI
    public String login(String email, String rawPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Lỗi: Sai email hoặc mật khẩu!"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Lỗi: Sai email hoặc mật khẩu!");
        }
        // Đúng hết thì in thẻ VIP trả về
        return jwtUtil.generateToken(email);
    }
}
