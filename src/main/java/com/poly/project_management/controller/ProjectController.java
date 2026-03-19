package com.poly.project_management.controller;

import com.poly.project_management.dto.ApiResponse;
import com.poly.project_management.entity.ProjectEntity;
import com.poly.project_management.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // API thêm nhân viên vào dự án
    @PostMapping("/{projectId}/add-member/{userId}")
    public ResponseEntity<ApiResponse<ProjectEntity>> addMember(@PathVariable Long projectId, @PathVariable Long userId) {
        ProjectEntity updatedProject = projectService.addMemberToProject(projectId, userId);
        ApiResponse<ProjectEntity> response = new ApiResponse<>(200, "Đã thêm nhân viên thành công vào dự án", updatedProject);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createProject() {
        return ResponseEntity.ok("Tuyệt vời! Sếp MANAGER đã tạo dự án thành công!");
    }
}