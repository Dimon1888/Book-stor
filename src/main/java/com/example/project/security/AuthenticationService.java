package com.example.project.security;

import com.example.project.dto.UserLoginRequestDto;
import com.example.project.dto.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto login(UserLoginRequestDto request);
}
