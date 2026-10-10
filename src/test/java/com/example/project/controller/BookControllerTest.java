package com.example.project.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.project.dto.BookDto;
import com.example.project.dto.BookSearchParametersDto;
import com.example.project.dto.CreateBookRequestDto;
import com.example.project.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private com.example.project.security.JwtUtil jwtUtil;

    private MockMvc mockMvc;

    private BookDto sampleBookDto;
    private CreateBookRequestDto sampleRequestDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        sampleBookDto = new BookDto();
        sampleBookDto.setId(1L);
        sampleBookDto.setTitle("Sample Book");
        sampleBookDto.setAuthor("Author");
        sampleBookDto.setIsbn("9780132350884");
        sampleBookDto.setPrice(BigDecimal.valueOf(29.99));
        sampleBookDto.setCategoryIds(List.of(1L));

        sampleRequestDto = new CreateBookRequestDto();
        sampleRequestDto.setTitle("Sample Book");
        sampleRequestDto.setAuthor("Author");
        sampleRequestDto.setIsbn("9780132350884");
        sampleRequestDto.setPrice(BigDecimal.valueOf(29.99));
        sampleRequestDto.setCategoryIds(List.of(1L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all books with pagination")
    void getAll_ValidPageable_ReturnsBookPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookService.getAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sampleBookDto), pageable, 1));

        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Sample Book")))
                .andExpect(jsonPath("$.content[0].price", is(29.99)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Search books dynamically with parameters")
    void searchBooks_ValidParameters_ReturnsBookPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookService.search(any(BookSearchParametersDto.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sampleBookDto), pageable, 1));

        mockMvc.perform(get("/books/search")
                        .param("titles", "Sample Book")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Sample Book")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get book by valid ID")
    void getBookById_ValidId_ReturnsBookDto() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(sampleBookDto);

        mockMvc.perform(get("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Sample Book")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a new book with ADMIN role")
    void createBook_ValidRequest_ReturnsCreatedBook() throws Exception {
        when(bookService.createBook(any(CreateBookRequestDto.class))).thenReturn(sampleBookDto);

        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Sample Book")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update existing book by ID with ADMIN role")
    void updateBook_ValidRequest_ReturnsUpdatedBook() throws Exception {
        when(bookService.update(eq(1L), any(CreateBookRequestDto.class))).thenReturn(sampleBookDto);

        mockMvc.perform(put("/books/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Sample Book")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete book by ID with ADMIN role")
    void deleteBook_ValidId_ReturnsNoContent() throws Exception {
        doNothing().when(bookService).deleteById(1L);

        mockMvc.perform(delete("/books/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(bookService).deleteById(1L);
    }
}
