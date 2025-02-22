package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.service.CategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/categories")
public class CategoryController implements CategoryControllerInterface{
    private final CategoryService categoryService;

    //Просмотр всех категорий товаров //localhost:8088/categories
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    //Добавление новой категории товаров //localhost:8088/categories
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createCategory(
            @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        return categoryService.createCategory(categoryRequestDto);
    }

    //Редактирование категории товаров по Id //localhost:8088/categories/3
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PutMapping(value = "/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponseDto updateCategory(
            @RequestBody @Valid CategoryRequestDto categoryRequestDto,
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long categoryId) {
        return categoryService.updateCategory(categoryRequestDto, categoryId);
    }

    //Удаление категории товаров по Id //localhost:8088/categories/11
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @DeleteMapping(value = "/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}

