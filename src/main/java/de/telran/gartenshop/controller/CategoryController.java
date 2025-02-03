package de.telran.gartenshop.controller;

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
public class CategoryController {
    private final CategoryService categoryService;

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
    public void deleteCategory(@PathVariable Long categoryId) { //delete
        categoryService.deleteCategory(categoryId);
    }
}

