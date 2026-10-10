package com.example.project.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.example.project.dto.CategoryDto;
import com.example.project.dto.CreateCategoryRequestDto;
import com.example.project.mapper.CategoryMapper;
import com.example.project.model.Category;
import com.example.project.repository.CategoryRepository;
import java.util.Optional;
import com.example.project.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Get category by valid ID - Returns CategoryDto")
    void getById_ValidId_ReturnsCategoryDto() {
        Long categoryId = 1L;
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto(categoryId, "Fiction", "Fiction description");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(categoryId);

        assertNotNull(result);
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    @DisplayName("Save new category - Returns CategoryDto")
    void save_ValidDto_ReturnsCategoryDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Fiction",
                "Fiction description"
        );
        Category category = new Category();
        CategoryDto expectedDto = new CategoryDto(1L, "Fiction", "Fiction description");

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        CategoryDto actual = categoryService.save(requestDto);

        assertNotNull(actual);
        verify(categoryRepository).save(category);
    }
}
