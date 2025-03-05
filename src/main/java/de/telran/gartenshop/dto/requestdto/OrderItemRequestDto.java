package de.telran.gartenshop.dto.requestdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequestDto {
    @NotNull(message = "ProductId cannot be null")
    @Min(value = 1, message = "Invalid productId: productId must be >= 1")
    private Long productId;

    @Min(value = 1, message = "Quantity must be >=1")
    private Integer quantity;
}
