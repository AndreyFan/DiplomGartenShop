package de.telran.gartenshop.dto.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {
    @Schema(description = "Identifier", required = true, example = "1")
    private Long categoryId;

    @Schema(description = "Name", example = "Garden Furniture")
    private String name;
}