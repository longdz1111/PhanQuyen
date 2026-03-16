package com.poly.project_management.controller;

import com.poly.project_management.entity.TaskEntity;
import com.poly.project_management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUser(@PathVariable Long userId) {
        List<TaskEntity> tasks = taskService.getTasksByUserId(userId);
        if (tasks.isEmpty()) {
            return ResponseEntity.ok("Nhân viên này hiện không có công việc nào.");
        }
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getTasksByProject(@PathVariable Long projectId) {
        List<TaskEntity> tasks = taskService.getTasksByProjectId(projectId);
        if (tasks.isEmpty()) {
            return ResponseEntity.ok("Dự án này hiện chưa có công việc nào.");
        }
        return ResponseEntity.ok(tasks);
    }
}