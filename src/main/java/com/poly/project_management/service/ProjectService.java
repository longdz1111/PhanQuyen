package com.poly.project_management.service;

import com.poly.project_management.entity.ProjectEntity;
import com.poly.project_management.entity.UserEntity;
import com.poly.project_management.repository.ProjectRepository;
import com.poly.project_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired private ProjectRepository projectRepository;
    @Autowired private UserRepository userRepository;

    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }
    // Tuần 5
    // Logic: Thêm nhân viên vào dự án
    public ProjectEntity addMemberToProject(Long projectId, Long userId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Lỗi: Không tìm thấy Dự án!"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Lỗi: Không tìm thấy Nhân viên!"));

        if (!project.getMembers().contains(user)) {
            project.getMembers().add(user);
        } else {
            throw new IllegalArgumentException("Lỗi: Nhân viên này đã ở trong dự án rồi!");
        }
        return projectRepository.save(project);
    }
}