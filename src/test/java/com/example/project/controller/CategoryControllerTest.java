package com.example.project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.project.dto.CategoryDto;
import com.example.project.dto.CreateCategoryRequestDto;
import com.example.project.security.JwtUtil;
import com.example.project.service.BookService;
import com.example.project.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    private static final String PATH = "/categories";

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all categories - Returns HTTP 200 OK")
    void getAll_ReturnsOk() throws Exception {
        CategoryDto categoryDto = new CategoryDto(1L, "Fantasy", "Fantasy books");
        PageImpl<CategoryDto> categoryPage = new PageImpl<>(List.of(categoryDto));

        when(categoryService.findAll(any(Pageable.class))).thenReturn(categoryPage);

        mockMvc.perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create category with valid DTO - Returns HTTP 201 Created")
    void createCategory_ValidDto_ReturnsCreated() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Fantasy",
                "Fantasy books"
        );
        CategoryDto responseDto = new CategoryDto(1L, "Fantasy", "Fantasy books");

        when(categoryService.save(any(CreateCategoryRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post(PATH)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
