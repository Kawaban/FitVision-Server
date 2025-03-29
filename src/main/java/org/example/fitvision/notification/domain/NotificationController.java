package org.example.fitvision.notification.domain;

import jakarta.validation.Valid;
import java.util.List;
import org.example.fitvision.notification.NotificationService;
import org.example.fitvision.notification.dto.FriendRequestCreate;
import org.example.fitvision.notification.dto.FriendRequestResponse;
import org.example.fitvision.notification.dto.NotificationResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public record NotificationController(NotificationService notificationService) {

    @PostMapping("/friend-request/send")
    void addFriendRequest(@Valid @RequestBody FriendRequestCreate friendRequest) {
        notificationService.addFriendRequest(friendRequest);
    }

    @PostMapping("/friend-request/accept")
    void acceptFriendRequest(@Valid @RequestBody FriendRequestResponse friendRequestId) {
        notificationService.acceptFriendRequest(friendRequestId);
    }

    @PostMapping("/friend-request/decline")
    void declineFriendRequest(@Valid @RequestBody FriendRequestResponse friendRequestId) {
        notificationService.declineFriendRequest(friendRequestId);
    }

    @GetMapping("/notifications")
    List<NotificationResponse> getNotifications() {
        return notificationService.getNotifications();
    }
}
