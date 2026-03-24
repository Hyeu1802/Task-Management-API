package com.example.Tuan9.repository;

import com.example.Tuan9.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
    List<TaskEntity> findByUserId(Integer userId);
    List<TaskEntity> findByProjectId(Integer projectId);
    List<TaskEntity> findByStatus(String status);
}