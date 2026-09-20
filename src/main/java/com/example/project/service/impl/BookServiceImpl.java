package com.example.project.service.impl;

import com.example.project.dto.BookDto;
import com.example.project.dto.BookDtoWithoutCategoryIds;
import com.example.project.dto.BookSearchParametersDto;
import com.example.project.dto.CreateBookRequestDto;
import com.example.project.exception.EntityNotFoundException;
import com.example.project.mapper.BookMapper;
import com.example.project.model.Book;
import com.example.project.repository.BookRepository;
import com.example.project.repository.book.BookSpecificationBuilder;
import com.example.project.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public Page<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public Page<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable) {
        Specification<Book> spec = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(spec, pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + id));
    }

    @Override
    @Transactional
    public BookDto createBook(CreateBookRequestDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(Long id, CreateBookRequestDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + id));
        bookMapper.updateBookFromDto(bookDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't delete book with id: " + id
                    + " because it doesn't exist");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDtoWithoutCategoryIds>
            getBooksByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoriesId(categoryId, pageable)
                .map(bookMapper::toDtoWithoutCategories);
    }
}
