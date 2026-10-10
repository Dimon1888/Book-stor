package com.example.project.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.example.project.dto.BookDto;
import com.example.project.exception.EntityNotFoundException;
import com.example.project.mapper.BookMapper;
import com.example.project.model.Book;
import com.example.project.repository.BookRepository;
import java.util.Optional;
import com.example.project.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Get book by valid ID - Returns BookDto")
    void getBookById_ValidId_ReturnsBookDto() {
        Long bookId = 1L;
        Book book = new Book();
        BookDto bookDto = new BookDto();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.getBookById(bookId);

        assertNotNull(result);
        verify(bookRepository).findById(bookId);
    }

    @Test
    @DisplayName("Get book by invalid ID - Throws EntityNotFoundException")
    void getBookById_InvalidId_ThrowsEntityNotFoundException() {
        Long invalidId = 999L;
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(invalidId));
    }
}
