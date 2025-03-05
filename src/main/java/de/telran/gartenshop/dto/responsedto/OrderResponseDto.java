package de.telran.gartenshop.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.telran.gartenshop.entity.enums.DeliveryMethod;
import de.telran.gartenshop.entity.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    @Schema(description = "Identifier", example = "1")
    private Long orderId;

    private Timestamp createdAt;

    @Schema(description = "Delivery Address", example = "35 Pine Street")
    private String deliveryAddress;

    @Schema(description = "Contact Phone", example = "0049876543")
    private String contactPhone;

    private DeliveryMethod deliveryMethod;
    private OrderStatus orderStatus;
    private Timestamp updatedAt;
    private Long userId;

    @JsonProperty("items")
    private Set<OrderItemResponseDto> orderItemsSet;
}
