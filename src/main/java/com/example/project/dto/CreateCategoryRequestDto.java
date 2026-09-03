package com.example.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotBlank(message = "Name cannot be empty")
        @Size(max = 255, message = "Name length must not exceed 255 characters")
        String name,

        @Size(max = 1000, message = "Description length must not exceed 1000 characters")
        String description
) {
}
