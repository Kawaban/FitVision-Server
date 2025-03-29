package org.example.fitvision.userinfo.domain;

import java.util.Base64;
import org.example.fitvision.userinfo.dto.FriendResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    @Mapping(source = "avatar", target = "avatar", qualifiedByName = "encodeImage")
    FriendResponse userInfoToFriendResponse(UserInfo userInfo);

    @Named("encodeImage")
    default String encodeImage(byte[] avatar) {
        return avatar != null ? Base64.getEncoder().encodeToString(avatar) : null;
    }
}
