package com.example.Tuan9.controller;

import com.example.Tuan9.dto.ApiResponse;
import com.example.Tuan9.entity.TaskEntity;
import com.example.Tuan9.entity.TaskStatus;
import com.example.Tuan9.entity.UserEntity;
import com.example.Tuan9.exception.CustomException;
import com.example.Tuan9.repository.UserRepository;
import com.example.Tuan9.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Controller", description = "Các API quản lý công việc")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Tạo công việc mới", description = "Yêu cầu quyền MANAGER hoặc có Token hợp lệ")
    @PostMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<TaskEntity>> createTask(@Valid @RequestBody TaskEntity task, @PathVariable Integer projectId) {
        TaskEntity result = taskService.createTask(task, projectId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Tạo Task thành công", result));
    }

    @Operation(summary = "Giao việc cho User", description = "Cập nhật người thực hiện công việc")
    @PutMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<ApiResponse<TaskEntity>> assignTask(@PathVariable Integer taskId, @PathVariable Integer userId) {
        TaskEntity result = taskService.assignTask(taskId, userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Giao việc thành công", result));
    }

    @Operation(summary = "Cập nhật trạng thái Task", description = "Chuyển trạng thái TODO, IN_PROGRESS, DONE")
    @PutMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<TaskEntity>> updateStatus(@PathVariable Integer taskId, @RequestParam TaskStatus status) {
        TaskEntity result = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật trạng thái thành công", result));
    }

    @Operation(summary = "Lấy danh sách task theo Project ID")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskEntity>>> getTasksByProject(@PathVariable Integer projectId) {
        List<TaskEntity> result = taskService.getTasksByProjectId(projectId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách thành công", result));
    }

    @Operation(summary = "Lấy danh sách task theo User ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TaskEntity>>> getTasksByUser(@PathVariable Integer userId) {
        List<TaskEntity> result = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách thành công", result));
    }

    @Operation(summary = "Lấy danh sách task của tôi", description = "Trả về danh sách task của user đang đăng nhập qua Token")
    @GetMapping("/my-tasks")
    public ResponseEntity<ApiResponse<List<TaskEntity>>> getMyTasks() {
        // Lấy username từ Token đang gửi lên
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Tìm UserEntity từ username
        UserEntity currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new CustomException("Không tìm thấy user", 404));

        // Lấy danh sách Task theo ID của user này
        List<TaskEntity> result = taskService.getTasksByUserId(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách task của tôi thành công", result));
    }
}