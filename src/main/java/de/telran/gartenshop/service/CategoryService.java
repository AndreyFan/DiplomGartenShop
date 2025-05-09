package de.telran.gartenshop.service;

import de.telran.gartenshop.configure.MapperUtil;
import de.telran.gartenshop.dto.requestdto.CategoryRequestDto;
import de.telran.gartenshop.dto.responsedto.CategoryResponseDto;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.exception.BadRequestException;
import de.telran.gartenshop.exception.DataNotFoundInDataBaseException;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
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
        try {
            CategoryEntity createCategoryEntity = mappers.convertToCategoryEntity(categoryRequestDto);
            CategoryEntity savedCategoryEntity = categoryRepository.save(createCategoryEntity);
            return savedCategoryEntity.getCategoryId() != null;
        } catch (Exception e) {
            throw new BadRequestException("Failed to create category: " + e.getMessage());
        }
    }

    public CategoryResponseDto updateCategory(CategoryRequestDto categoryRequestDto, Long categoryId) {
        CategoryEntity updateCategoryEntity = categoryRepository.findById(categoryId).orElse(null);

        if (updateCategoryEntity != null) {
            updateCategoryEntity.setName(categoryRequestDto.getName());
            categoryRepository.save(updateCategoryEntity);
        } else {
            throw new DataNotFoundInDataBaseException("Category not found with Id: " + categoryId);
        }
        return mappers.convertToCategoryResponseDto(updateCategoryEntity);
    }

    public void deleteCategory(Long categoryId) {
        CategoryEntity deleteCategoryEntity = categoryRepository.findById(categoryId).orElse(null);
        if (deleteCategoryEntity != null) {
            try {
                categoryRepository.delete(deleteCategoryEntity);
            } catch (Exception exception) {
                throw new IllegalArgumentException("Cannot delete due to integrity constraints Category with Id: " + categoryId);
            }
        } else {
            throw new DataNotFoundInDataBaseException("Category not found with Id: " + categoryId);
        }
    }
}
