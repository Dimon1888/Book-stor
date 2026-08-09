package com.example.project.service.impl;

import com.example.project.dto.UserRegistrationRequestDto;
import com.example.project.dto.UserResponseDto;
import com.example.project.exception.RegistrationException;
import com.example.project.mapper.UserMapper;
import com.example.project.model.Role;
import com.example.project.model.RoleName;
import com.example.project.model.User;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.user.UserRepository;
import com.example.project.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User with email "
                    + request.getEmail() + " already exists");
        }

        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RegistrationException("Default role ROLE_USER not found"));
        user.setRoles(Set.of(defaultRole));

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
