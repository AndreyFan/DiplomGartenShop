package de.telran.gartenshop.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.telran.gartenshop.entity.OrderEntity;
import de.telran.gartenshop.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDto {
    private Long orderItemId;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private ProductResponseDto product;
    private OrderResponseDto order;
}
