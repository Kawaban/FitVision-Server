package org.example.fitvision.notification.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByEmailRecipient(String emailRecipient);

    Optional<Notification> findByNotificationObjectUuid(UUID notificationObjectUuid);

    Optional<Notification> findByEmailSenderAndEmailRecipientAndType(
            String emailSender, String emailRecipient, NotificationType type);
}
