package com.example.Tuan9.controller;

import com.example.Tuan9.entity.UserEntity;
import com.example.Tuan9.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserEntity user) {
        try {
            UserEntity newUser = userService.createUser(user);
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            // Hứng lỗi cơ bản và trả về HTTP Status 400
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}