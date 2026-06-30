package com.example.project.service;

import com.example.project.dto.BookDto;
import com.example.project.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    List<BookDto> getAll();

    BookDto getBookById(Long id);

    BookDto createBook(CreateBookRequestDto requestDto);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);
}
