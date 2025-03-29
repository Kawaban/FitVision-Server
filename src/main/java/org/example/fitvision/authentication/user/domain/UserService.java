package org.example.fitvision.authentication.user.domain;

import java.util.UUID;
import lombok.val;
import org.example.fitvision.authentication.user.dto.UserRequest;
import org.example.fitvision.infrastructure.exception.ApplicationEntityNotFoundException;
import org.example.fitvision.infrastructure.exception.UserAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
record UserService(UserRepository userRepository, UserMapper userMapper)
        implements org.example.fitvision.authentication.user.UserService {
    @Override
    public void createUser(UserRequest userRequest) {
        if (existsByEmail(userRequest.email())) {
            throw new UserAlreadyExistsException();
        }
        val user = userMapper.userRequestToUser(userRequest);
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws ApplicationEntityNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(ApplicationEntityNotFoundException::new);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void changeEmail(String oldEmail, String newEmail) {
        val user = userRepository.findByEmail(oldEmail).orElseThrow(ApplicationEntityNotFoundException::new);
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(UUID userId, String password) {
        val user = userRepository.findById(userId).orElseThrow(ApplicationEntityNotFoundException::new);
        user.setPassword(password.toCharArray());
        userRepository.save(user);
    }

    @Override
    public UUID findUserIdByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(ApplicationEntityNotFoundException::new)
                .getUuid();
    }
}
