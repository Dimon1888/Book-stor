package com.example.project.service.impl;

import com.example.project.dto.UserRegistrationRequestDto;
import com.example.project.dto.UserResponseDto;
import com.example.project.exception.RegistrationException;
import com.example.project.mapper.UserMapper;
import com.example.project.model.User;
import com.example.project.repository.user.UserRepository;
import com.example.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) throws
            RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User from email "
                    + request.getEmail() + " already exists");
        }

        User user = userMapper.toModel(request);
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }
}
