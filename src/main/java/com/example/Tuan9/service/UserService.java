package com.example.Tuan9.service;

import com.example.Tuan9.entity.UserEntity;
import com.example.Tuan9.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity createUser(UserEntity user) {
        // Xử lý lỗi cơ bản: Trùng email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trong hệ thống!");
        }
        return userRepository.save(user);
    }
}