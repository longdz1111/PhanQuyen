package com.poly.project_management.entity;

import com.poly.project_management.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên công việc không được để trống!")
    @Size(min = 5, max = 255, message = "Tên công việc phải từ 5 đến 255 ký tự!")
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @FutureOrPresent(message = "Deadline phải là ngày hôm nay hoặc trong tương lai!")
    @Column(name = "deadline")
    private LocalDate deadline;
    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private TaskStatus status = TaskStatus.TODO;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity assignee;

    public TaskEntity() {}

    // --- Getters và Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public ProjectEntity getProject() { return project; }
    public void setProject(ProjectEntity project) { this.project = project; }

    public UserEntity getAssignee() { return assignee; }
    public void setAssignee(UserEntity assignee) { this.assignee = assignee; }
}