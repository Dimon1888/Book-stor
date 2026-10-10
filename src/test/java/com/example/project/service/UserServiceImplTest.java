package com.example.project.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.example.project.dto.UserRegistrationRequestDto;
import com.example.project.dto.UserResponseDto;
import com.example.project.exception.RegistrationException;
import com.example.project.mapper.UserMapper;
import com.example.project.model.Role;
import com.example.project.model.RoleName;
import com.example.project.model.User;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.user.UserRepository;
import java.util.Optional;
import com.example.project.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ShoppingCartService shoppingCartService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Register user with existing email - Throws RegistrationException")
    void register_ExistingEmail_ThrowsRegistrationException() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail("test@example.com");

        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        assertThrows(RegistrationException.class, () -> userService.register(requestDto));
    }

    @Test
    @DisplayName("Register user successfully - Returns UserResponseDto")
    void register_ValidRequest_ReturnsUserResponseDto() throws RegistrationException {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail("newuser@example.com");
        requestDto.setPassword("password123");

        User user = new User();
        Role role = new Role();
        role.setName(RoleName.ROLE_USER);

        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userMapper.toModel(requestDto)).thenReturn(user);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new UserResponseDto());

        UserResponseDto actual = userService.register(requestDto);

        assertNotNull(actual);
        verify(shoppingCartService).createShoppingCartForUser(user);
    }
}
