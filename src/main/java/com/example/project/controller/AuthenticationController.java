package com.example.project.controller;

import com.example.project.dto.UserLoginRequestDto;
import com.example.project.dto.UserLoginResponseDto;
import com.example.project.dto.UserRegistrationRequestDto;
import com.example.project.dto.UserResponseDto;
import com.example.project.exception.RegistrationException;
import com.example.project.security.AuthenticationService;
import com.example.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Management", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(summary = "New user registration", description = "Registering "
            + "a new user in the system")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.login(request);
    }
}
