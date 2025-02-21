package de.telran.gartenshop.controller;

import de.telran.gartenshop.aspect.LogAnnotation;
import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.service.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.createCategory(categoryRequestDto);
    }

    //Редактирование категории товаров по Id //localhost:8088/categories/3
    @PutMapping(value = "/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponseDto updateCategory(@RequestBody CategoryRequestDto categoryRequestDto, @PathVariable Long categoryId) {
        return categoryService.updateCategory(categoryRequestDto, categoryId);
    }

    //Удаление категории товаров по Id //localhost:8088/categories/11
    @DeleteMapping(value = "/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable Long categoryId) { //delete
        categoryService.deleteCategory(categoryId);
    }
}

