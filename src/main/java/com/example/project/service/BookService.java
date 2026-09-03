package com.example.project.service;

import com.example.project.dto.BookDto;
import com.example.project.dto.BookDtoWithoutCategoryIds;
import com.example.project.dto.BookSearchParametersDto;
import com.example.project.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<BookDto> getAll(Pageable pageable);

    Page<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable);

    BookDto getBookById(Long id);

    BookDto createBook(CreateBookRequestDto bookDto);

    BookDto update(Long id, CreateBookRequestDto bookDto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long categoryId, Pageable pageable);
}
