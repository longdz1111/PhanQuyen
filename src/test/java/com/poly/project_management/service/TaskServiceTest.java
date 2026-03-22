package com.poly.project_management.service;

import com.poly.project_management.entity.ProjectEntity;
import com.poly.project_management.entity.TaskEntity;
import com.poly.project_management.entity.UserEntity;
import com.poly.project_management.enums.TaskStatus;
import com.poly.project_management.repository.ProjectRepository;
import com.poly.project_management.repository.TaskRepository;
import com.poly.project_management.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    //  1: Tạo Task thành công & Verify behavior

    @Test
    public void testCreateTask_Success() {
        Long projectId = 1L;
        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);

        TaskEntity requestTask = new TaskEntity();
        requestTask.setTitle("Code tính năng giỏ hàng");

        TaskEntity savedTask = new TaskEntity();
        savedTask.setTitle("Code tính năng giỏ hàng");
        savedTask.setStatus(TaskStatus.TODO);
        savedTask.setProject(project);

        // Dạy DB giả: Tồn tại Project ID = 1
        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        // Dạy DB giả: Khi save thì trả về savedTask
        Mockito.when(taskRepository.save(Mockito.any(TaskEntity.class))).thenReturn(savedTask);

        TaskEntity result = taskService.createTask(projectId, requestTask);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Code tính năng giỏ hàng", result.getTitle());
        Assertions.assertEquals(TaskStatus.TODO, result.getStatus());


        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any(TaskEntity.class));
    }
    //2: Test case lỗi (Tên công việc để trống)
    @Test
    public void testCreateTask_Fail_EmptyTitle() {
        Long projectId = 1L;
        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);
        TaskEntity requestTask = new TaskEntity();
        requestTask.setTitle("   ");
        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(projectId, requestTask)
        );
        Assertions.assertEquals("Lỗi: Tên công việc không được để trống!", exception.getMessage());
    }


    //3: Test rule assign task (Nhân viên không thuộc dự án)
    @Test
    public void testAssignTask_Fail_UserNotInProject() {
        Long taskId = 1L;
        Long userId = 99L;

        // Giả lập Project (danh sách thành viên TRỐNG)
        ProjectEntity project = new ProjectEntity();
        project.setMembers(new ArrayList<>());

        // Giả lập Task thuộc về Project trên
        TaskEntity task = new TaskEntity();
        task.setId(taskId);
        task.setProject(project);

        // Giả lập User
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setFullName("Bảo Long");

        // Dạy DB giả trả về Task và User
        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Gọi hàm assignTask và bắt lỗi
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.assignTask(taskId, userId)
        );
        // So sánh
        Assertions.assertEquals("Lỗi: Nhân viên Bảo Long CHƯA tham gia dự án này, không thể giao việc!", exception.getMessage());
    }
}