package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Categories-Endpoint", description = "Controller for managing categories",
        externalDocs = @ExternalDocumentation(description = "Link for external " +
                "documentation in German language", url = "https://gartenshopExDoc.de"
        )
)
@Validated
public interface CategoryControllerInterface {

    @Operation(summary = "All Categories", description = "Let`s get all the categories")
    public List<CategoryResponseDto> getAllCategories();

    @Operation(summary = "Create a new Category", description = "Creates a new category by adding the provided" +
            " category details to the system")
    public boolean createCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto);

    @Operation(summary = "Update a Category", description = "Opportunity to update an existing Category " +
            "with the provided details.")
    public CategoryResponseDto updateCategory(
            @RequestBody @Valid CategoryRequestDto categoryRequestDto,
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long categoryId);

    @Operation(summary = "Delete a Category from DB", description = "Finds and deletes an existing Category from the database.")
    public void deleteCategory(
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long categoryId);
}