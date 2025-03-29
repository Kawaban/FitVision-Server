package org.example.fitvision.notification.domain;

import java.util.List;
import lombok.val;
import org.example.fitvision.infrastructure.exception.ApplicationEntityNotFoundException;
import org.example.fitvision.infrastructure.exception.BadInputException;
import org.example.fitvision.infrastructure.utils.InstantToStringFormatter;
import org.example.fitvision.notification.dto.FriendRequestCreate;
import org.example.fitvision.notification.dto.FriendRequestResponse;
import org.example.fitvision.notification.dto.NotificationResponse;
import org.example.fitvision.userinfo.UserInfoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
record NotificationService(
        UserInfoService userInfoService,
        FriendRequestRepository friendRequestRepository,
        NotificationRepository notificationRepository,
        InstantToStringFormatter instantToStringFormatter)
        implements org.example.fitvision.notification.NotificationService {

    @Override
    public void addFriendRequest(FriendRequestCreate friendRequest) throws ApplicationEntityNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = (UserDetails) authentication.getPrincipal();
        val sender = userInfoService.getUserInfoByEmail(user.getUsername());

        if (sender.getEmail().equals(friendRequest.emailRecipient())) {
            throw new BadInputException();
        }

        // Check if there is already a friend request between the two users
        val existingNotification = notificationRepository.findByEmailSenderAndEmailRecipientAndType(
                sender.getEmail(), friendRequest.emailRecipient(), NotificationType.FRIEND_REQUEST);
        val existingNotificationReverse = notificationRepository.findByEmailSenderAndEmailRecipientAndType(
                friendRequest.emailRecipient(), sender.getEmail(), NotificationType.FRIEND_REQUEST);

        if (existingNotification.isPresent()) {
            if (!existingNotification.get().isRead()) {
                throw new BadInputException();
            }
        }

        if (existingNotificationReverse.isPresent()) {
            if (!existingNotificationReverse.get().isRead()) {
                throw new BadInputException();
            }
        }

        val recipient = userInfoService.getUserInfoByEmail(friendRequest.emailRecipient());
        val friendRequestObject = friendRequestRepository.save(
                FriendRequest.builder().sender(sender).receiver(recipient).build());
        val notification = Notification.builder()
                .type(NotificationType.FRIEND_REQUEST)
                .emailSender(user.getUsername())
                .emailRecipient(friendRequest.emailRecipient())
                .notificationObjectUuid(friendRequestObject.getUuid())
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public void acceptFriendRequest(FriendRequestResponse friendRequestResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = (UserDetails) authentication.getPrincipal();
        val friendRequest = friendRequestRepository
                .findByUuid(friendRequestResponse.id())
                .orElseThrow(ApplicationEntityNotFoundException::new);

        val notificationObject = notificationRepository
                .findByNotificationObjectUuid(friendRequest.getUuid())
                .orElseThrow(ApplicationEntityNotFoundException::new);
        notificationObject.setRead(true);
        notificationRepository.save(notificationObject);

        userInfoService.addFriend(friendRequest.getSender(), friendRequest.getReceiver());
        val notification = Notification.builder()
                .type(NotificationType.FRIEND_REQUEST_ACCEPTED)
                .emailSender(user.getUsername())
                .emailRecipient(friendRequest.getSender().getEmail())
                .build();
        notificationRepository.save(notification);
        friendRequestRepository.delete(friendRequest);
    }

    @Override
    public void declineFriendRequest(FriendRequestResponse friendRequestResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = (UserDetails) authentication.getPrincipal();
        val friendRequest = friendRequestRepository
                .findByUuid(friendRequestResponse.id())
                .orElseThrow(ApplicationEntityNotFoundException::new);

        val notificationObject = notificationRepository
                .findByNotificationObjectUuid(friendRequest.getUuid())
                .orElseThrow(ApplicationEntityNotFoundException::new);
        notificationObject.setRead(true);
        notificationRepository.save(notificationObject);

        val notification = Notification.builder()
                .type(NotificationType.FRIEND_REQUEST_DECLINED)
                .emailSender(user.getUsername())
                .emailRecipient(friendRequest.getSender().getEmail())
                .build();
        notificationRepository.save(notification);

        friendRequestRepository.delete(friendRequest);
    }

    @Override
    public List<NotificationResponse> getNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = (UserDetails) authentication.getPrincipal();
        val notifications = notificationRepository.findAllByEmailRecipient(user.getUsername());
        val result = notifications.stream()
                .filter(notification -> !notification.isRead())
                .map(notification -> NotificationResponse.builder()
                        .type(notification.getType().name())
                        .emailSender(notification.getEmailSender())
                        .id(
                                (notification.getType() != NotificationType.FRIEND_REQUEST)
                                        ? notification.getUuid()
                                        : notification.getNotificationObjectUuid())
                        .nickSender(userInfoService
                                .getUserInfoByEmail(notification.getEmailSender())
                                .getName())
                        .time(instantToStringFormatter.format(notification.getCreatedDate()))
                        .build())
                .toList();
        notifications.stream()
                .filter(notification -> notification.getType() != NotificationType.FRIEND_REQUEST)
                .forEach(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                });
        return result;
    }
}
