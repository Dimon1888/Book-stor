package com.example.project.dto;

public record BookSearchParametersDto(
        String title,
        String author,
        String isbn
) {
}
