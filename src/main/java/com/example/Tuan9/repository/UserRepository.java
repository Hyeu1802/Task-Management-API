package com.example.Tuan9.repository;

import com.example.Tuan9.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    // Kế thừa sẵn các hàm findAll, findById, save, delete...
    // Có thể viết thêm hàm query theo email:
    boolean existsByEmail(String email);

    Optional<UserEntity> findByUsername(String username);
}
