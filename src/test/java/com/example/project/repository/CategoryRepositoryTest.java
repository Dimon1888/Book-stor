package com.example.project.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.example.project.model.Category;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=none"
})
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Save category and find it by ID successfully")
    void saveAndFindById_ValidCategory_ReturnsCategory() {
        // Given
        Category category = new Category();
        category.setName("Fiction");
        category.setDescription("Fiction books category");

        Category savedCategory = categoryRepository.save(category);
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getId()).isEqualTo(savedCategory.getId());
        assertThat(foundCategory.get().getName()).isEqualTo("Fiction");
        assertThat(foundCategory.get().getDescription()).isEqualTo("Fiction books category");
    }

    @Test
    @DisplayName("Delete category by ID successfully")
    void deleteCategory_ValidId_CategoryIsRemoved() {
        // Given
        Category category = new Category();
        category.setName("Science");
        category.setDescription("Science books category");
        Category savedCategory = categoryRepository.save(category);
        Long categoryId = savedCategory.getId();

        categoryRepository.deleteById(categoryId);
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);

        assertThat(foundCategory).isNotPresent();
    }
}
