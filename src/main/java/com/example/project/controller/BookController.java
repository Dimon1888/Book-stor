package com.example.project.controller;

import com.example.project.dto.BookDto;
import com.example.project.dto.BookSearchParametersDto;
import com.example.project.dto.CreateBookRequestDto;
import com.example.project.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book Management", description = "Endpoints for managing books")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Get a paginated list "
            + "of all available books")
    public Page<BookDto> getAll(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search for books dynamically"
            + " using specifications with pagination")
    public Page<BookDto> searchBooks(
            BookSearchParametersDto searchParameters,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable
    ) {
        return bookService.search(searchParameters, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Get details of a specific book by its ID")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new book", description = "Add a new book to the store")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Update the details of"
            + " an existing book by its ID")
    public BookDto updateBook(@PathVariable Long id, @RequestBody @Valid
            CreateBookRequestDto bookDto) {
        return bookService.update(id, bookDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book", description = "Soft delete a book by its ID")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
