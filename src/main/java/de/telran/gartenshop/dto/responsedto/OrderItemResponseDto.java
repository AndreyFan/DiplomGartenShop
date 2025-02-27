package de.telran.gartenshop.dto.responsedto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDto {
    private Long orderItemId;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private ProductResponseDto product;
}
