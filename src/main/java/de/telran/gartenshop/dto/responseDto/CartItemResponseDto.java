package de.telran.gartenshop.dto.responseDto;

//CartItems - товары в корзине

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDto {
    @Schema(description = "Identifier", example = "1")
    private Long cartItemId;

    @Schema(description = "Quantity", example = "12")
    private Integer quantity;

    @JsonProperty("cart")
    private CartResponseDto cartResponseDto;

    @JsonProperty("product")
    private ProductResponseDto productResponseDto;
}
