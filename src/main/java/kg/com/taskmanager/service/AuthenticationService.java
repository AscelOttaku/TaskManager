package kg.com.taskmanager.service;

import kg.com.taskmanager.dto.auth.AuthenticationResponse;
import kg.com.taskmanager.dto.auth.SignInRequest;
import kg.com.taskmanager.dto.auth.SignUpRequest;

public interface AuthenticationService {
    AuthenticationResponse authenticate(SignUpRequest signUpRequest);

    AuthenticationResponse signIn(SignInRequest signInRequest);
}
