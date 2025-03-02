package de.telran.gartenshop.dto.requestdto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Name", example = "Watering Can")
    @Size(min = 2, max = 50, message = "Invalid name: Must be of 2 - 50 characters")
    private String name;

    @Schema(description = "Description", example = "Large capacity watering can for plants")
    @Size(min = 2, max = 150, message = "Invalid description: Must be of 2 - 150 characters")
    private String description;

    @Schema(description = "Price", example = "9.99")
    @DecimalMin(value = "0.00", message = "Invalid price: must be >= 0")
    @Digits(integer = 7, fraction = 2, message = "Invalid price: Must be a number with up to 7 digits before and 2 after the decimal.")
    private BigDecimal price;

    @Schema(description = "Identifier", example = "1")
    @Min(value = 1, message = "Invalid categoryId: categoryId must be >= 1")
    private Long categoryId;

    @Schema(description = "ImageUrl", example = "https://example.com/images/watering_can.jpg")
    @Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid URL")
    private String imageUrl;

    @Schema(description = "Discount Price", example = "8.49")
    @DecimalMin(value = "0.00", message = "Invalid discountPrice: must be >= 0")
    @Digits(integer = 7, fraction = 2, message = "Invalid price: Must be a number with up to 7 digits before and 2 after the decimal.")
    private BigDecimal discountPrice;
}