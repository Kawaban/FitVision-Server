package org.example.fitvision.authentication.domain;

import lombok.val;
import org.example.fitvision.authentication.dto.LoginRequest;
import org.example.fitvision.authentication.dto.LoginResponse;
import org.example.fitvision.authentication.dto.RegisterRequest;
import org.example.fitvision.authentication.user.UserService;
import org.example.fitvision.authentication.user.dto.UserRequest;
import org.example.fitvision.jwt.JwtService;
import org.example.fitvision.jwt.dto.JwtUser;
import org.example.fitvision.userinfo.UserInfoService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public record AuthenticationService(
        PasswordEncoder passwordEncoder,
        UserService userService,
        UserInfoService userInfoService,
        JwtService jwtService,
        AuthenticationManager authenticationManager)
        implements org.example.fitvision.authentication.AuthenticationService {

    @Override
    public void register(RegisterRequest registerRequest) {
        System.out.println(registerRequest.email());
        System.out.println(registerRequest.name());
        val userRequest = UserRequest.builder()
                .email(registerRequest.email())
                .password(passwordEncoder
                        .encode(new String(registerRequest.password()))
                        .toCharArray())
                .build();
        userService.createUser(userRequest);

        System.out.println(registerRequest.email());
        System.out.println(registerRequest.name());

        val userInfoRequest = org.example.fitvision.userinfo.dto.UserInfoRequest.builder()
                .email(registerRequest.email())
                .name(registerRequest.name())
                .build();
        userInfoService.createUserInfo(userInfoRequest);

        registerRequest.zeroPassword();
        userRequest.zeroPassword();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        val user = userService.loadUserByUsername(loginRequest.email());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.email(), new String(loginRequest.password()), user.getAuthorities()));
        loginRequest.zeroPassword();

        val token = jwtService().generateToken(new JwtUser(user.getUsername()));
        return new LoginResponse(token);
    }
}
