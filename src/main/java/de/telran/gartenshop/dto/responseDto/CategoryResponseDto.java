package de.telran.gartenshop.dto.responseDto;

import lombok.*;


public class CategoryResponseDto {
    private Long categoryId;
    private String name;

    public CategoryResponseDto() {
    }

    public CategoryResponseDto(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }
}