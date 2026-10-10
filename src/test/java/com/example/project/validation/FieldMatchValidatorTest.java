package com.example.project.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.example.project.dto.UserRegistrationRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FieldMatchValidatorTest {

    private FieldMatchValidator validator;

    @Mock
    private FieldMatch constraintAnnotation;

    @BeforeEach
    void setUp() {
        validator = new FieldMatchValidator();
        when(constraintAnnotation.first()).thenReturn("password");
        when(constraintAnnotation.second()).thenReturn("repeatPassword");
        validator.initialize(constraintAnnotation);
    }

    @Test
    @DisplayName("Valid passwords match - Returns true")
    void isValid_MatchingFields_ReturnsTrue() {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto();
        dto.setPassword("password123");
        dto.setRepeatPassword("password123");

        boolean result = validator.isValid(dto, null);

        assertTrue(result);
    }

    @Test
    @DisplayName("Invalid passwords do not match - Returns false")
    void isValid_MismatchedFields_ReturnsFalse() {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto();
        dto.setPassword("password123");
        dto.setRepeatPassword("differentPassword");

        boolean result = validator.isValid(dto, null);

        assertFalse(result);
    }

    @Test
    @DisplayName("Both fields are null - Returns true")
    void isValid_NullFields_ReturnsTrue() {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto();

        boolean result = validator.isValid(dto, null);

        assertTrue(result);
    }
}
