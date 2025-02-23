package de.telran.gartenshop.service;

import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.exception.BadRequestException;
import de.telran.gartenshop.exception.DataNotFoundInDataBaseException;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Mappers mappers;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryEntity categoryEntity;
    private CategoryRequestDto categoryRequestDto;
    private CategoryResponseDto categoryResponseDto;

    @BeforeEach
    void setUp() {
        categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(1L);
        categoryEntity.setName("TestCategory");

        categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("TestCategory");

        categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setCategoryId(1L);
        categoryResponseDto.setName("TestCategory");
    }

    @Test
    void getAllCategoriesTest() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(categoryEntity));
        when(mappers.convertToCategoryResponseDto(any())).thenReturn(categoryResponseDto);

        List<CategoryResponseDto> categoryResponseDtos = categoryService.getAllCategories();

        assertNotNull(categoryResponseDtos);
        assertEquals(1, categoryResponseDtos.size());
        assertEquals("TestCategory", categoryResponseDtos.get(0).getName());
    }

    @Test
    void createCategoryTest() {
        when(mappers.convertToCategoryEntity(any())).thenReturn(categoryEntity);
        when(categoryRepository.save(any())).thenReturn(categoryEntity);

        boolean isCreated = categoryService.createCategory(categoryRequestDto);
        assertTrue(isCreated);
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void createCategoryFalseTest() {
        when(mappers.convertToCategoryEntity(any())).thenReturn(categoryEntity);
        categoryEntity.setCategoryId(null);
        when(categoryRepository.save(any())).thenReturn(categoryEntity);

        boolean isCreated = categoryService.createCategory(categoryRequestDto);
        assertFalse(isCreated);
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void createCategoryExceptionTest() {
        when(mappers.convertToCategoryEntity(any())).thenReturn(categoryEntity);
        when(categoryRepository.save(any())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> categoryService.createCategory(null));
    }

    @Test
    void updateCategoryTest() {
        when(mappers.convertToCategoryResponseDto(any())).thenReturn(categoryResponseDto);
        when(categoryRepository.save(any())).thenReturn(categoryEntity);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(categoryEntity));
        CategoryResponseDto updateCategory = categoryService.updateCategory(categoryRequestDto, 1L);
        assertNotNull(updateCategory);
        assertEquals("TestCategory", updateCategory.getName());
    }

    @Test
    void updateCategoryExceptionByCategoryTest() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(DataNotFoundInDataBaseException.class, () -> categoryService.updateCategory(null, 1L));
    }

    @Test
    void deleteCategoryTest() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(categoryEntity));
        doNothing().when(categoryRepository).delete(any());
        assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
    }

    @Test
    void deleteCategoryExceptionByCategoryTest() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(DataNotFoundInDataBaseException.class, () -> categoryService.deleteCategory(1L));
    }

    @Test
    void deleteCategoryExceptionTest() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryEntity));
        doThrow(IllegalArgumentException.class).when(categoryRepository).delete(categoryEntity);
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(1L));
    }
}