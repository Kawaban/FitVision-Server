package org.example.fitvision.infrastructure.scheduler;

import org.example.fitvision.task.TaskService;
import org.example.fitvision.userinfo.UserInfoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public record EndOfDayScheduler(TaskService taskService, UserInfoService userInfoService) {

    @Scheduled(cron = "0 40 21 * * *")
    public void endOfDay() {
        userInfoService.getAllUsers().forEach(taskService::scheduledEndOfDay);
    }
}
