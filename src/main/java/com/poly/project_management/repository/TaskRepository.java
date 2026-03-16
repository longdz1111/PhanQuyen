package com.poly.project_management.repository;

import com.poly.project_management.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByAssigneeId(Long userId);
    List<TaskEntity> findByProjectId(Long projectId);
}