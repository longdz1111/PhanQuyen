package com.poly.project_management.filter;

import com.poly.project_management.service.CustomUserDetailsService;
import com.poly.project_management.until.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired private JwtUtil jwtUtil;
    @Autowired private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Chặn người dùng lại, đòi xem thẻ (nằm ở thẻ Authorization trên Postman)
        String header = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // 2. Thẻ JWT chuẩn luôn bắt đầu bằng chữ "Bearer "
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7); // Cắt bỏ chữ Bearer đi để lấy nguyên cái mã Token
            if (jwtUtil.validateToken(token)) {
                email = jwtUtil.extractEmail(token); // Nếu thẻ thật, đọc tên người dùng ra
            }
        }

        // 3. Nếu có tên rồi mà chưa được cấp phép vào cửa thì cấp phép cho vào
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 4. Mở barie cho đi tiếp
        filterChain.doFilter(request, response);
    }
}