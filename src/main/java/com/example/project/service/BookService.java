package com.example.project.service;

import com.example.project.dto.BookDto;
import com.example.project.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    List<BookDto> getAll(); // замість findAll()

    BookDto getBookById(Long id); // замість findById()

    BookDto createBook(CreateBookRequestDto requestDto); // замістьproject.
}
