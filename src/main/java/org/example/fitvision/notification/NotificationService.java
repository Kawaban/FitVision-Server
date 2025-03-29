package org.example.fitvision.notification;

import java.util.List;
import org.example.fitvision.notification.dto.FriendRequestCreate;
import org.example.fitvision.notification.dto.FriendRequestResponse;
import org.example.fitvision.notification.dto.NotificationResponse;

public interface NotificationService {
    void addFriendRequest(FriendRequestCreate friendRequest);

    void acceptFriendRequest(FriendRequestResponse friendRequestResponse);

    void declineFriendRequest(FriendRequestResponse friendRequestResponse);

    List<NotificationResponse> getNotifications();
}
