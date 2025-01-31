package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequiredArgsConstructor
@RequestMapping(value = "/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.createCategory(categoryRequestDto);
    }

    @PutMapping(value = "/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponseDto updateCategory(@RequestBody CategoryRequestDto categoryRequestDto, @PathVariable Long categoryId) {
        return categoryService.updateCategory(categoryRequestDto, categoryId);
    }

    @DeleteMapping(value = "/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategories(@PathVariable Long categoryId) { //delete
        categoryService.deleteCategory(categoryId);
    }
}

