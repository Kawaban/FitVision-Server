package org.example.fitvision.userinfo.domain;

import lombok.val;
import org.example.fitvision.task.TaskService;
import org.example.fitvision.userinfo.UserInfoService;
import org.example.fitvision.userinfo.dto.UserInfoResponse;
import org.springframework.stereotype.Service;

@Service
public record UserInfoResponseService(UserInfoService userInfoService, TaskService taskService) {
    public UserInfoResponse getUserInfoWithTasks() {
        val userInfoDto = userInfoService.getUserInfoWithOutTasks();
        userInfoDto.activeTasks().addAll(taskService.getActiveTasks());
        userInfoDto.preparedTasks().addAll(taskService.getPreparedTasks());
        userInfoDto.allTasks().addAll(taskService.getTasks());
        return userInfoDto;
    }
}
