package de.telran.gartenshop.service;

import de.telran.gartenshop.configure.MapperUtil;
import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class CategoryService {
    private CategoryRepository categoryRepository;
    private Mappers mappers;

    public CategoryService(CategoryRepository categoryRepository, Mappers mappers) {
        this.categoryRepository = categoryRepository;
        this.mappers = mappers;
    }

    public List<CategoryResponseDto> getAllCategories() {
        List<CategoryEntity> categoriesEntityList = categoryRepository.findAll();
        return MapperUtil.convertList(categoriesEntityList, mappers::convertToCategoryResponseDto);
    }

    public boolean createCategory(CategoryRequestDto categoryRequestDto) {
        CategoryEntity createCategoryEntity = mappers.convertToCategoryEntity(categoryRequestDto);
        CategoryEntity savedCategoryEntity = categoryRepository.save(createCategoryEntity);
        return createCategoryEntity.getCategoryId() != null;
    }

    public CategoryResponseDto updateCategory(CategoryRequestDto categoryRequestDto, Long categoryId) {

        CategoryEntity updateCategoryEntity = new CategoryEntity(categoryId, categoryRequestDto.getName());
        CategoryEntity returnCategoryEntity = categoryRepository.save(updateCategoryEntity);
        return new CategoryResponseDto(returnCategoryEntity.getCategoryId(), returnCategoryEntity.getName());
    }
}
