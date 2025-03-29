package org.example.fitvision.task.domain;

import java.util.List;
import java.util.UUID;
import org.example.fitvision.userinfo.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findAllByUser(UserInfo user);
    //    List<Task> findAllByUserAndActiveTrue(UserInfo user);
}
