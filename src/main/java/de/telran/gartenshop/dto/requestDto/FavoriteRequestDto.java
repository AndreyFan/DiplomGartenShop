package de.telran.gartenshop.dto.requestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequestDto {

    @NotNull(message = "ProductId cannot be null")
    @Min(value = 1, message = "Invalid productId: productId must be >= 1")
    private Long productId;

    @NotNull(message = "UserId cannot be null")
    @Min(value = 1, message = "Invalid userId: userId must be >= 1")
    private Long userId;
}
