package org.example.fitvision.authentication;

import org.example.fitvision.authentication.dto.LoginRequest;
import org.example.fitvision.authentication.dto.LoginResponse;
import org.example.fitvision.authentication.dto.RegisterRequest;

public interface AuthenticationService {
    void register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);
}
