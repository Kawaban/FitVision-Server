package org.example.fitvision.notification.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.fitvision.infrastructure.model.AbstractEntity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "notifications")
public class Notification extends AbstractEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(nullable = false)
    private String emailRecipient;

    @Column(nullable = false)
    private String emailSender;

    @Column(nullable = false)
    private boolean isRead;

    private UUID notificationObjectUuid = null;

    @Builder
    public Notification(
            UUID uuid,
            Long version,
            Instant createdDate,
            Instant lastModifiedDate,
            NotificationType type,
            String emailRecipient,
            String emailSender,
            UUID notificationObjectUuid) {
        super(uuid, version, createdDate, lastModifiedDate);
        this.type = type;
        this.emailRecipient = emailRecipient;
        this.emailSender = emailSender;
        isRead = false;
        this.notificationObjectUuid = notificationObjectUuid;
    }
}
