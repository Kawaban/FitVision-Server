package org.example.fitvision.notification.dto;

import jakarta.validation.constraints.NotNull;

public record FriendRequestCreate(@NotNull String emailRecipient) {}
