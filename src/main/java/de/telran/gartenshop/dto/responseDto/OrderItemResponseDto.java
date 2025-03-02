package de.telran.gartenshop.dto.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDto {
    @Schema(description = "Identifier", example = "1")
    private Long orderItemId;

    @Schema(description = "Quantity", example = "2")
    private Integer quantity;

    @Schema(description = "Price at Purchase", example = "240,5")
    private BigDecimal priceAtPurchase;

    private ProductResponseDto product;
}
