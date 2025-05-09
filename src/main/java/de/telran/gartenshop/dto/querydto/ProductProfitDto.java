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
public class ProductProfitDto {
    @Schema(description = "Period", example = "DAY")
    private String period;

    @Schema(description = "Profit", example = "60")
    private BigDecimal profit;
}