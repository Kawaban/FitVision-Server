package org.example.fitvision.task;

import java.util.List;
import org.example.fitvision.task.dto.*;
import org.example.fitvision.userinfo.domain.UserInfo;

public interface TaskService {
    void createTask(CreateTaskRequest createTaskRequest);

    List<TaskResponse> getTasks();

    void updateTask(TaskUpdate taskUpdate);

    void deleteTask(String taskName);

    void completeTasks(List<TaskCompletion> taskCompletion);

    void prepareTasks(TaskPreparation taskPreparation);

    void scheduledEndOfDay(UserInfo userInfo);

    List<TaskResponse> getPreparedTasks();

    List<TaskResponse> getActiveTasks();
}
