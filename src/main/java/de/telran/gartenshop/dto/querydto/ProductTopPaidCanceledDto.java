package de.telran.gartenshop.dto.querydto;

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
    private Integer productId;
    private String productName;
    private Long saleFrequency;
    private BigDecimal saleQuantity;
    private BigDecimal saleSumma;
}