package com.example.Tuan9.service;

import com.example.Tuan9.entity.*;
import com.example.Tuan9.exception.CustomException;
import com.example.Tuan9.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Kích hoạt Mockito
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    // Inject các Mock Repository (Tạo repo giả mạo)
    @Mock private TaskRepository taskRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private UserRepository userRepository;

    // Bơm các Mock vào trong TaskService
    @InjectMocks
    private TaskService taskService;

    // Dữ liệu mẫu dùng chung cho các test case
    private TaskEntity sampleTask;
    private ProjectEntity sampleProject;
    private UserEntity sampleUser;

    @BeforeEach
    void setUp() {
        sampleProject = new ProjectEntity();
        sampleProject.setId(1);
        sampleProject.setName("Dự án Test");

        sampleUser = new UserEntity();
        sampleUser.setId(1);
        sampleUser.setUsername("hieu_test");

        sampleTask = new TaskEntity();
        sampleTask.setId(10);
        sampleTask.setTitle("Task Test Mockito");
        sampleTask.setDeadline(LocalDate.now().plusDays(5));
    }

    // ================== TEST HÀM CREATE TASK ==================

    @Test
    void createTask_Success_ReturnsTask() {
        // 1. Chuẩn bị (Arrange)
        when(projectRepository.findById(1)).thenReturn(Optional.of(sampleProject));
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // 2. Thực thi (Act)
        TaskEntity result = taskService.createTask(sampleTask, 1);

        // 3. Kiểm chứng (Assert & Verify behavior)
        assertNotNull(result);
        assertEquals(TaskStatus.TODO, result.getStatus()); // Đảm bảo trạng thái mặc định là TODO
        assertEquals(sampleProject, result.getProject());  // Đảm bảo đã gắn đúng Project

        // Xác minh hành vi (verify) là hàm save() đã thực sự được gọi 1 lần
        verify(taskRepository, times(1)).save(any(TaskEntity.class));
    }

    @Test
    void createTask_ProjectNotFound_ThrowsCustomException() {
        // Arrange: Giả lập database không tìm thấy Project
        when(projectRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            taskService.createTask(sampleTask, 99);
        });

        assertEquals(404, exception.getStatus());
        assertTrue(exception.getMessage().contains("Không tìm thấy Project ID"));

        // Verify: Đảm bảo vì lỗi nên hàm save() KHÔNG BAO GIỜ được gọi
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    // ================== TEST HÀM ASSIGN TASK ==================

    @Test
    void assignTask_Success_ReturnsAssignedTask() {
        // Arrange
        when(taskRepository.findById(10)).thenReturn(Optional.of(sampleTask));
        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        TaskEntity result = taskService.assignTask(10, 1);

        // Assert
        assertNotNull(result.getUser());
        assertEquals("hieu_test", result.getUser().getUsername());

        // Verify behavior
        verify(taskRepository, times(1)).save(sampleTask);
    }

    @Test
    void assignTask_TaskNotFound_ThrowsException() {
        // Arrange
        when(taskRepository.findById(88)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            taskService.assignTask(88, 1);
        });

        assertEquals(404, exception.getStatus());
        verify(userRepository, never()).findById(any()); // Nếu lỗi task thì không cần tìm user nữa
    }
}