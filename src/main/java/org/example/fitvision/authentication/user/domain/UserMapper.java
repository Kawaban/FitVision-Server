package org.example.fitvision.authentication.user.domain;

import org.example.fitvision.authentication.user.dto.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface UserMapper {

    User userRequestToUser(UserRequest user);
}
