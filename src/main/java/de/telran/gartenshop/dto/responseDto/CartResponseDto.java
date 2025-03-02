package de.telran.gartenshop.dto.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDto {
    @Schema(description = "Identifier", example = "1")
    private Long cartId;

    @Schema(description = "Identifier", example = "1")
    private Long userId;
}