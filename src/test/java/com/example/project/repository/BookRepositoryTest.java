package com.example.project.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.example.project.model.Book;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by category ID - Returns page of books")
    @Sql(scripts = {
            "classpath:database/categories/add-categories.sql",
            "classpath:database/books/add-books.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/remove-books.sql",
            "classpath:database/categories/remove-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoriesId_ValidCategoryId_ReturnsPage() {
        Long categoryId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);

        Page<Book> result = bookRepository.findAllByCategoriesId(categoryId, pageable);

        assertFalse(result.isEmpty());
        List<Book> books = result.getContent();
        assertEquals(2, books.size());
    }
}
