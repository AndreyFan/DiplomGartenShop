package de.telran.gartenshop.dto.querydto;

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
    private Integer productId;
    private String productName;
    private Integer orderId;
    private Timestamp createdAt;
    private Integer quantity;
}