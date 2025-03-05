package de.telran.gartenshop.dto.querydto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAwaitingPaymentDto {
    @Schema(description = "Identifier", example = "6")
    private Integer productId;

    @Schema(description = "Product Name", example = "Fungicide Powder")
    private String productName;

    @Schema(description = "OrderID", example = "3")
    private Integer orderId;

    private Timestamp createdAt;

    @Schema(description = "Quantity", example = "5")
    private Integer quantity;
}