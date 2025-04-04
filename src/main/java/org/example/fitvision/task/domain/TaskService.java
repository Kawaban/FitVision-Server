package org.example.fitvision.task.domain;

import java.util.Base64;
import java.util.List;
import lombok.val;
import org.example.fitvision.infrastructure.exception.ApplicationEntityNotFoundException;
import org.example.fitvision.infrastructure.exception.TaskAlreadyExistsException;
import org.example.fitvision.task.dto.*;
import org.example.fitvision.userinfo.domain.UserInfo;
import org.example.fitvision.userinfo.domain.UserInfoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public record TaskService(TaskRepository taskRepository, UserInfoService userInfoService, TaskMapper taskMapper)
        implements org.example.fitvision.task.TaskService {

    @Override
    public void createTask(CreateTaskRequest createTaskRequest) {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = (UserDetails) authentication.getPrincipal();
        val userInfo = userInfoService.getUserInfoByEmail(user.getUsername());

        if (userInfo.getTasks().stream().anyMatch(task -> task.getTaskName().equals(createTaskRequest.taskName()))) {
            throw new TaskAlreadyExistsException();
        }

        val task = taskMapper.toTask(createTaskRequest);
        task.setUser(userInfo);
        taskRepository.save(task);
    }

    @Override
    public List<TaskResponse> getTasks() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = (UserDetails) authentication.getPrincipal();
        val userInfo = userInfoService.getUserInfoByEmail(user.getUsername());
        return userInfo.getTasks().stream().map(taskMapper::toTaskResponse).toList();
    }

    @Override
    public void updateTask(TaskUpdate taskUpdate) {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = (UserDetails) authentication.getPrincipal();
        val userInfo = userInfoService.getUserInfoByEmail(user.getUsername());
        val task = userInfo.getTasks().stream()
                .filter(t -> t.getTaskName().equals(taskUpdate.taskOldName()))
                .findFirst()
                .orElseThrow(ApplicationEntityNotFoundException::new);

        task.setTaskImage(Base64.getDecoder().decode(taskUpdate.taskImage()));
        task.setTaskName(taskUpdate.taskNewName());
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(String taskName) {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = (UserDetails) authentication.getPrincipal();
        val userInfo = userInfoService.getUserInfoByEmail(user.getUsername());

        val task = userInfo.getTasks().stream()
                .filter(t -> t.getTaskName().equals(taskName))
                .findFirst()
                .orElseThrow(ApplicationEntityNotFoundException::new);

        taskRepository.delete(task);
    }

    @Override
    public void completeTasks(List<TaskCompletion> taskCompletion) {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = (UserDetails) authentication.getPrincipal();
        val userInfo = userInfoService.getUserInfoByEmail(user.getUsername());

        userInfo.getTasks().stream()
                .filter(t ->
                        taskCompletion.stream().anyMatch(tc -> tc.taskName().equals(t.getTaskName())))
                .forEach(t -> {
                    t.setCompleted(taskCompletion.stream()
                            .filter(tc -> tc.taskName().equals(t.getTaskName()))
                            .findFirst()
                            .get()
                            .status());
                    taskRepository.save(t);
                });
    }

    @Override
    public void prepareTasks(TaskPreparation taskPreparation) {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = (UserDetails) authentication.getPrincipal();
        val userInfo = userInfoService.getUserInfoByEmail(user.getUsername());

        userInfo.getTasks().forEach(t -> {
            t.setPrepared(false);
            taskRepository.save(t);
        });

        userInfo.getTasks().stream()
                .filter(t -> taskPreparation.taskName().contains(t.getTaskName()))
                .forEach(t -> {
                    t.setPrepared(true);
                    taskRepository.save(t);
                });
    }

    @Override
    public void scheduledEndOfDay(UserInfo userInfo) {

        if (userInfo.getTasks().stream().filter(Task::isActive).allMatch(Task::isCompleted)) {
            userInfoService.wonDay(userInfo);
        } else {
            userInfoService.lostDay(userInfo);
        }

        userInfo.getTasks().forEach(t -> {
            t.setActive(t.isPrepared());
            t.setCompleted(false);
            taskRepository.save(t);
        });
    }

    @Override
    public List<TaskResponse> getPreparedTasks() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = (UserDetails) authentication.getPrincipal();
        val userInfo = userInfoService.getUserInfoByEmail(user.getUsername());
        return userInfo.getTasks().stream()
                .filter(Task::isPrepared)
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getActiveTasks() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = (UserDetails) authentication.getPrincipal();
        val userInfo = userInfoService.getUserInfoByEmail(user.getUsername());
        return userInfo.getTasks().stream()
                .filter(Task::isActive)
                .map(taskMapper::toTaskResponse)
                .toList();
    }
}
