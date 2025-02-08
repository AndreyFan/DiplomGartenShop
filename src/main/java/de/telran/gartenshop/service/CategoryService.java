package de.telran.gartenshop.service;

import de.telran.gartenshop.configure.MapperUtil;
import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final Mappers mappers;

    public List<CategoryResponseDto> getAllCategories() {
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        return MapperUtil.convertList(categoryEntityList, mappers::convertToCategoryResponseDto);
    }

    public boolean createCategory(CategoryRequestDto categoryRequestDto) {
        CategoryEntity createCategoryEntity = mappers.convertToCategoryEntity(categoryRequestDto);
        CategoryEntity savedCategoryEntity = categoryRepository.save(createCategoryEntity);
        if (savedCategoryEntity == null) {
            throw new HttpMessageConversionException("Category " + categoryRequestDto.getName() + " not created");
        }
        return savedCategoryEntity.getCategoryId() != null;
    }

    public CategoryResponseDto updateCategory(CategoryRequestDto categoryRequestDto, Long categoryId) {
        CategoryEntity updateCategoryEntity = categoryRepository.findById(categoryId).orElse(null);

        if (updateCategoryEntity != null) {
            updateCategoryEntity.setName(categoryRequestDto.getName());
            categoryRepository.save(updateCategoryEntity);
        } else {
            throw new NullPointerException("Category not found with Id: " + categoryId);
        }
        return mappers.convertToCategoryResponseDto(updateCategoryEntity);
    }

    public void deleteCategory(Long categoryId) {
        CategoryEntity deleteCategoryEntity = categoryRepository.findById(categoryId).orElse(null);
        if (deleteCategoryEntity != null) {
            try {
                categoryRepository.delete(deleteCategoryEntity);
            } catch (Exception exception) {
                throw new NullPointerException("Cannot delete due to integrity constraints Category with Id: " + categoryId);
            }
        } else {
            throw new NullPointerException("Category not found with Id: " + categoryId);
        }
    }
}
