package org.example.fitvision.userinfo.dto;

import lombok.Builder;

@Builder
public record UserInfoRequest(String email, String name) {}
