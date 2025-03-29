package org.example.fitvision.userinfo.dto;

import java.util.List;
import org.example.fitvision.task.dto.TaskResponse;

public record UserInfoResponse(
        String email,
        String name,
        String avatar,
        int streak,
        int longestStreak,
        int completedTasks,
        int wonDays,
        int daysIn,
        List<TaskResponse> allTasks,
        List<TaskResponse> preparedTasks,
        List<TaskResponse> activeTasks) {}
