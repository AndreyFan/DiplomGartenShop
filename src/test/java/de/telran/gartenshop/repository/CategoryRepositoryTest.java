package de.telran.gartenshop.repository;

import de.telran.gartenshop.entity.CategoryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findAll() {
        List<CategoryEntity> categoriesTest = categoryRepository.findAll();
        assertNotNull(categoriesTest);

    }

    @Test
    void testSaveAndFindCategory() {
        CategoryEntity category = new CategoryEntity();
        category.setName("Flowers");

        CategoryEntity savedCategory = categoryRepository.save(category);
        Optional<CategoryEntity> foundCategory = categoryRepository.findById(savedCategory.getCategoryId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Flowers");
    }

    @Test
    void testUpdateCategory() {
        CategoryEntity category = new CategoryEntity();
        category.setName("Garden");

        CategoryEntity savedCategory = categoryRepository.save(category);
        savedCategory.setName("Updated Garden");
        CategoryEntity updatedCategory = categoryRepository.save(savedCategory);

        Optional<CategoryEntity> foundCategory = categoryRepository.findById(updatedCategory.getCategoryId());
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Updated Garden");
    }

    @Test
    void testDeleteCategory() {
        CategoryEntity category = new CategoryEntity();
        category.setName("Trees");

        CategoryEntity savedCategory = categoryRepository.save(category);
        categoryRepository.delete(savedCategory);

        Optional<CategoryEntity> foundCategory = categoryRepository.findById(savedCategory.getCategoryId());
        assertThat(foundCategory).isEmpty();
    }
}
