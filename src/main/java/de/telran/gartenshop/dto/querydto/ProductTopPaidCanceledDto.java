package de.telran.gartenshop.dto.querydto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTopPaidCanceledDto {
    @Schema(description = "Identifier", example = "15")
    private Integer productId;

    @Schema(description = "Product Name", example = "Hose with Spray Nozzle")
    private String productName;

    @Schema(description = "Sale Frequency", example = "1")
    private Long saleFrequency;

    @Schema(description = "Sale Quantity", example = "4")
    private BigDecimal saleQuantity;

    @Schema(description = "Sale Sum", example = "2560")
    private BigDecimal saleSum;
}