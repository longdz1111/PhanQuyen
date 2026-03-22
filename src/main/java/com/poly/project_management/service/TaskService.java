package com.poly.project_management.service;

import com.poly.project_management.entity.ProjectEntity;
import com.poly.project_management.entity.TaskEntity;
import com.poly.project_management.entity.UserEntity;
import com.poly.project_management.enums.TaskStatus;
import com.poly.project_management.repository.ProjectRepository;
import com.poly.project_management.repository.TaskRepository;
import com.poly.project_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private UserRepository userRepository;

    public List<TaskEntity> getTasksByUserId(Long userId) { return taskRepository.findByAssigneeId(userId); }
    public List<TaskEntity> getTasksByProjectId(Long projectId) { return taskRepository.findByProjectId(projectId); }

    public TaskEntity createTask(Long projectId, TaskEntity newTask) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Lỗi: Không tìm thấy Dự án có ID = " + projectId));

        if (newTask.getTitle() == null || newTask.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Lỗi: Tên công việc không được để trống!");
        }

        newTask.setProject(project);
        newTask.setStatus(TaskStatus.TODO);
        return taskRepository.save(newTask);
    }

    public TaskEntity assignTask(Long taskId, Long userId) {
        // 👉 ĐÃ REFACTOR: Dùng hàm getTaskById() rút gọn code trùng lặp
        TaskEntity task = getTaskById(taskId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Lỗi: Không tìm thấy Nhân viên!"));

        boolean isMember = task.getProject().getMembers().stream()
                .anyMatch(member -> member.getId().equals(userId));

        if (!isMember) {
            throw new IllegalArgumentException("Lỗi: Nhân viên " + user.getFullName() + " CHƯA tham gia dự án này, không thể giao việc!");
        }

        task.setAssignee(user);
        if (task.getStatus() == TaskStatus.TODO) {
            task.setStatus(TaskStatus.IN_PROGRESS);
        }
        return taskRepository.save(task);
    }

    public TaskEntity updateTaskStatus(Long taskId, TaskStatus newStatus) {
        // 👉 ĐÃ REFACTOR: Dùng hàm getTaskById() rút gọn code trùng lặp
        TaskEntity task = getTaskById(taskId);

        // MỤC 8: Chặn update
        if (task.getStatus() == TaskStatus.DONE) {
            throw new IllegalArgumentException("Lỗi: Task này đã hoàn thành (DONE), bạn không thể thay đổi trạng thái được nữa!");
        }

        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    private TaskEntity getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Lỗi: Không tìm thấy Task!"));
    }
}