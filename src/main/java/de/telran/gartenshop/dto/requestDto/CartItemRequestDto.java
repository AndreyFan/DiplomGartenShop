package de.telran.gartenshop.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//4) CartItems - товары в корзине
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequestDto {
    private Long productId;
    private Integer quantity;
}
