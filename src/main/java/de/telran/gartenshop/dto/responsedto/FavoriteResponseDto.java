package de.telran.gartenshop.dto.responsedto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponseDto {
    @Schema(description = "Identifier", example = "2")
    private Long favoriteId;
    private UserResponseDto userResponseDto;
    private ProductResponseDto productResponseDto;
}
