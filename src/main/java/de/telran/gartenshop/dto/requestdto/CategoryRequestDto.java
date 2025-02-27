package de.telran.gartenshop.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDto {
    @NotBlank(message = "Invalid name: Empty name")
    @Size(min = 2, max = 50, message = "Invalid category name: Must be of 2 - 50 characters")
    private String name;
}

