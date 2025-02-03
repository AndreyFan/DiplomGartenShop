package de.telran.gartenshop.dto.responseDto;

//CartItems - товары в корзине

import de.telran.gartenshop.dto.requestDto.CartRequestDto;
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
    private CartRequestDto cart;
    private ProductResponseDto product;


}
