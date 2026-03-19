package com.poly.project_management.controller;

import com.poly.project_management.dto.ApiResponse;
import com.poly.project_management.entity.TaskEntity;
import com.poly.project_management.enums.TaskStatus;
import com.poly.project_management.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired private TaskService taskService;

    // 1 Lấy Task theo User
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TaskEntity>>> getTasksByUser(@PathVariable Long userId) {
        List<TaskEntity> tasks = taskService.getTasksByUserId(userId);
        ApiResponse<List<TaskEntity>> response = new ApiResponse<>(200, "Lấy danh sách thành công", tasks);
        return ResponseEntity.ok(response);
    }


    // 2 Lấy Task theo Project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskEntity>>> getTasksByProject(@PathVariable Long projectId) {
        List<TaskEntity> tasks = taskService.getTasksByProjectId(projectId);
        ApiResponse<List<TaskEntity>> response = new ApiResponse<>(200, "Lấy danh sách thành công", tasks);
        return ResponseEntity.ok(response);
    }

    // 3Tạo Task Mới
    @PostMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<TaskEntity>> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody TaskEntity taskRequest) {

        TaskEntity newTask = taskService.createTask(projectId, taskRequest);
        ApiResponse<TaskEntity> response = new ApiResponse<>(201, "Tạo công việc thành công", newTask);
        return ResponseEntity.ok(response);
    }

    // 4 Giao việc
    @PutMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<ApiResponse<TaskEntity>> assignTask(@PathVariable Long taskId, @PathVariable Long userId) {
        TaskEntity updatedTask = taskService.assignTask(taskId, userId);
        ApiResponse<TaskEntity> response = new ApiResponse<>(200, "Đã giao việc thành công!", updatedTask);
        return ResponseEntity.ok(response);
    }

    // 5 Cập nhật trạng thái
    @PutMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<TaskEntity>> updateTaskStatus(@PathVariable Long taskId, @RequestParam TaskStatus status) {
        TaskEntity updatedTask = taskService.updateTaskStatus(taskId, status);
        ApiResponse<TaskEntity> response = new ApiResponse<>(200, "Đã cập nhật trạng thái thành công!", updatedTask);
        return ResponseEntity.ok(response);
    }
}