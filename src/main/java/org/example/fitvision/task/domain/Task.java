package org.example.fitvision.task.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.fitvision.infrastructure.model.AbstractEntity;
import org.example.fitvision.userinfo.domain.UserInfo;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "tasks")
public class Task extends AbstractEntity {

    @Column(nullable = false)
    private String taskName;

    @Column(columnDefinition = "bytea")
    private byte[] taskImage;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    private UserInfo user;

    @Column(nullable = false)
    private boolean isCompleted;

    @Column(nullable = false)
    private boolean isActive;

    @Column
    private boolean isPrepared;

    @Builder
    public Task(
            UUID uuid,
            Long version,
            Instant createdDate,
            Instant lastModifiedDate,
            String taskName,
            byte[] taskImage,
            UserInfo user) {
        super(uuid, version, createdDate, lastModifiedDate);
        this.taskName = taskName;
        this.taskImage = taskImage;
        this.user = user;
        this.isActive = false;
        this.isCompleted = false;
        this.isPrepared = false;
    }
}
