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

    // MỤC 3 & 4: Tạo Task & Validate projectId
    public TaskEntity createTask(Long projectId, TaskEntity taskRequest) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Lỗi: Không tìm thấy Dự án có ID = " + projectId));

        if (taskRequest.getTitle() == null || taskRequest.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Lỗi: Tên công việc không được để trống!");
        }

        taskRequest.setProject(project);
        taskRequest.setStatus(TaskStatus.TODO);
        return taskRepository.save(taskRequest);
    }

    // MỤC 5 & 6: Assign Task & Kiểm tra user có thuộc project không
    public TaskEntity assignTask(Long taskId, Long userId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Lỗi: Không tìm thấy Task!"));
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

    // MỤC 7 & 8: Update Status & Chặn nếu đã DONE
    public TaskEntity updateTaskStatus(Long taskId, TaskStatus newStatus) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Lỗi: Không tìm thấy Task!"));

        // MỤC 8: Chặn update
        if (task.getStatus() == TaskStatus.DONE) {
            throw new IllegalArgumentException("Lỗi: Task này đã hoàn thành (DONE), bạn không thể thay đổi trạng thái được nữa!");
        }

        task.setStatus(newStatus);
        return taskRepository.save(task);
    }
}