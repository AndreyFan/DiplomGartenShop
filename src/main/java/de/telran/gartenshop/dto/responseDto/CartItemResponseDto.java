package de.telran.gartenshop.dto.responseDto;

//CartItems - товары в корзине

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDto {
    private Long cartItemId;
    private Integer quantity;

    @JsonProperty("cart")
    private CartResponseDto cartResponseDto;

    @JsonProperty("product")
    private ProductResponseDto productResponseDto;
}
