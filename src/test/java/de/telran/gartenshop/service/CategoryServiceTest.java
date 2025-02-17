package de.telran.gartenshop.service;

import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@ActiveProfiles(profiles = {"test"})
@TestPropertySource("classpath:application-test.properties")
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAllCategories() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("test");
        categoryService.createCategory(requestDto);
        CategoryRequestDto requestDto1 = new CategoryRequestDto();
        requestDto1.setName("test1");
        categoryService.createCategory(requestDto1);
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        assertEquals(2, categories.size());

    }

    @Test
    void testCreateCategory() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Garden Tools");

        assertTrue(categoryService.createCategory(requestDto));

        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        assertFalse(categories.isEmpty());
        assertEquals("Garden Tools", categories.get(0).getName());
    }

    @Test
    void updateCategory() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Garden Tools");

        categoryService.createCategory(requestDto);
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        assertFalse(categories.isEmpty());
        CategoryResponseDto createdCategory = categories.stream()
                .filter(c -> "Garden Tools".equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Long categoryId = createdCategory.getCategoryId();
        CategoryRequestDto updateRequestDto = new CategoryRequestDto();
        updateRequestDto.setName("Garden Tools");
        CategoryResponseDto updateCategory = categoryService.updateCategory(updateRequestDto, categoryId);
        assertEquals("Garden Tools", updateCategory.getName());

    }



    @Test
    void deleteCategory() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Garden Tools");
        categoryService.createCategory(requestDto);
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        Long categoryId = categories.get(0).getCategoryId();
        categoryService.deleteCategory(categoryId);
        List<CategoryResponseDto> categoriesAfterDelete = categoryService.getAllCategories();
        assertTrue(categoriesAfterDelete.isEmpty());
    }
}