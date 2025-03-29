package org.example.fitvision.authentication.domain;

import org.example.fitvision.authentication.dto.LoginRequest;
import org.example.fitvision.authentication.dto.LoginResponse;
import org.example.fitvision.authentication.dto.RegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
record AuthenticationController(AuthenticationService authenticationService) {

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest registerRequest) {
        authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }
}
