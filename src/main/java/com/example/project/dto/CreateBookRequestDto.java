package com.example.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateBookRequestDto {
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Size(min = 1, max = 255, message = "Author name must be between 1 and 255 characters")
    private String author;

    @NotBlank(message = "ISBN cannot be blank")
    @Pattern(
            regexp = "^(978|979)?\\d{9}(\\d|X)$",
            message = "Invalid ISBN format. Must be a valid ISBN-10 or ISBN-13"
    )
    private String isbn;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price cannot be less than 0")
    private BigDecimal price;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private String coverImage;

    private List<Long> categoryIds;
}
