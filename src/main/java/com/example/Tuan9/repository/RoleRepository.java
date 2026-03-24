package com.example.Tuan9.repository;

import com.example.Tuan9.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    // Hàm này giúp Spring tự động sinh câu query: SELECT * FROM roles WHERE name = ?
    Optional<RoleEntity> findByName(String name);
}