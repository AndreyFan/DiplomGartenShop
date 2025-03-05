package de.telran.gartenshop.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    @Schema(description = "Identifier", example = "3")
    private Long productId;

    @Schema(description = "Name", example = "Watering Can")
    private String name;

    @Schema(description = "Description", example = "Large capacity watering can for plants")
    private String description;

    @Schema(description = "Price", example = "9.99")
    private BigDecimal price;

    @Schema(description = "ImageUrl", example = "images/watering_can.jpg")
    private String imageUrl;

    @Schema(description = "Discount Price", example = "8.49")
    private BigDecimal discountPrice;

    @Schema(description = "Created at", example = "2025-01-01")
    private Timestamp createdAt;

    @Schema(description = "Updated at", example = "2025-01-01")
    private Timestamp updatedAt;

    @JsonProperty("category")
    private CategoryResponseDto categoryResponseDto;
}