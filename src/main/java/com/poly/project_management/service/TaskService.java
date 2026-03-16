package com.poly.project_management.service;

import com.poly.project_management.entity.TaskEntity;
import com.poly.project_management.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<TaskEntity> getTasksByUserId(Long userId) {
        return taskRepository.findByAssigneeId(userId);
    }

    public List<TaskEntity> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }
}