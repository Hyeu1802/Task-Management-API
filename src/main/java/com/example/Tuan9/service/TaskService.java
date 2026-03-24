package com.example.Tuan9.service;

import com.example.Tuan9.entity.*;
import com.example.Tuan9.exception.CustomException;
import com.example.Tuan9.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private UserRepository userRepository;

    // === REFACTOR: Gom các đoạn code tìm kiếm trùng lặp thành hàm helper (Private) ===
    private TaskEntity getTaskById(Integer taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException("Không tìm thấy Task ID: " + taskId, 404));
    }

    private ProjectEntity getProjectById(Integer projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException("Không tìm thấy Project ID: " + projectId, 404));
    }

    private UserEntity getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Không tìm thấy User ID: " + userId, 404));
    }
    // =================================================================================

    // Hàm Tạo Task (Đã đổi tên tham số cho rõ nghĩa)
    public TaskEntity createTask(TaskEntity newTask, Integer targetProjectId) {
        ProjectEntity project = getProjectById(targetProjectId); // Dùng hàm helper gọn hơn

        newTask.setProject(project);
        newTask.setStatus(TaskStatus.TODO);
        return taskRepository.save(newTask);
    }

    // Hàm Giao việc
    public TaskEntity assignTask(Integer taskId, Integer assigneeId) {
        TaskEntity task = getTaskById(taskId);
        UserEntity assignee = getUserById(assigneeId);

        task.setUser(assignee);
        return taskRepository.save(task);
    }

    // Hàm Update trạng thái
    public TaskEntity updateTaskStatus(Integer taskId, TaskStatus newStatus) {
        TaskEntity task = getTaskById(taskId);

        if (task.getStatus() == TaskStatus.DONE) {
            throw new CustomException("Không thể cập nhật Task đã DONE!", 400);
        }
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    // Các hàm Get list (Giữ nguyên)
    public List<TaskEntity> getTasksByUserId(Integer userId) {
        return taskRepository.findByUserId(userId);
    }

    public List<TaskEntity> getTasksByProjectId(Integer projectId) {
        return taskRepository.findByProjectId(projectId);
    }
}