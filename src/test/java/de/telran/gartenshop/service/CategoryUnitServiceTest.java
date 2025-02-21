package de.telran.gartenshop.service;

import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.entity.CategoryEntity;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryUnitServiceTest {


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
    void getAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(categoryEntity));
        when(mappers.convertToCategoryResponseDto(any())).thenReturn(categoryResponseDto);

        List<CategoryResponseDto> categoryResponseDtos = categoryService.getAllCategories();

        assertNotNull(categoryResponseDtos);
        assertEquals(1, categoryResponseDtos.size());
        assertEquals("TestCategory", categoryResponseDtos.get(0).getName());
    }

    @Test
    void createCategory() {
        when(mappers.convertToCategoryEntity(any())).thenReturn(categoryEntity);
        when(categoryRepository.save(any())).thenReturn(categoryEntity);

        boolean isCreated = categoryService.createCategory(categoryRequestDto);
        assertTrue(isCreated);
    }

    @Test
    void updateCategory() {
        when(mappers.convertToCategoryResponseDto(any())).thenReturn(categoryResponseDto);
        when(categoryRepository.save(any())).thenReturn(categoryEntity);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(categoryEntity));
        CategoryResponseDto updateCategory = categoryService.updateCategory(categoryRequestDto, 1L);
        assertNotNull(updateCategory);
        assertEquals("TestCategory", updateCategory.getName());
    }

    @Test
    void deleteCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(categoryEntity));
        doNothing().when(categoryRepository).delete(any());
        assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
    }


}
