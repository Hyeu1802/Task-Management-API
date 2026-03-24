package com.example.Tuan9.controller;

import com.example.Tuan9.dto.ApiResponse;
import com.example.Tuan9.entity.RoleEntity;
import com.example.Tuan9.entity.UserEntity;
import com.example.Tuan9.repository.RoleRepository;
import com.example.Tuan9.repository.UserRepository;
import com.example.Tuan9.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;

    // 1. API REGISTER (Có băm mật khẩu)
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UserEntity user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Username đã tồn tại", null));
        }

        // Băm mật khẩu bằng BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Mặc định gán role ROLE_USER khi đăng ký
        RoleEntity userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Chưa cài đặt Role trong DB"));
        user.getRoles().add(userRole);

        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse<>(200, "Đăng ký thành công", user.getUsername()));
    }

    // 2. API LOGIN (Cấp phát Token)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        try {
            // Xác thực với Spring Security
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            // Nếu thành công -> Sinh Token
            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(new ApiResponse<>(200, "Đăng nhập thành công", token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Sai tài khoản hoặc mật khẩu", null));
        }
    }
}