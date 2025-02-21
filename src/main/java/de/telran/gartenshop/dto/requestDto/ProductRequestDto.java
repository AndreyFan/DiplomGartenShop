package de.telran.gartenshop.dto.requestDto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {
    @Size(min = 2, max = 50, message = "Invalid name: Must be of 2 - 50 characters")
    private String name;

    @Size(min = 2, max = 150, message = "Invalid description: Must be of 2 - 150 characters")
    private String description;

    @DecimalMin(value = "0.00", message = "Invalid price: must be >= 0")
    @Digits(integer = 7, fraction = 2, message = "Invalid price: Must be a number with up to 7 digits before and 2 after the decimal.")
    private BigDecimal price;

    @NotNull(message = "CategoryId cannot be null")
    @Min(value = 1, message = "Invalid categoryId: categoryId must be >= 1")
    private Long categoryId;

    @Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid URL")
    private String imageUrl;

    @DecimalMin(value = "0.00", message = "Invalid discountPrice: must be >= 0")
    @Digits(integer = 7, fraction = 2, message = "Invalid price: Must be a number with up to 7 digits before and 2 after the decimal.")
    private BigDecimal discountPrice;
}