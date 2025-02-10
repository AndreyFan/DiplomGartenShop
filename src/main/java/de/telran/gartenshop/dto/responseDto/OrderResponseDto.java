package de.telran.gartenshop.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.telran.gartenshop.entity.enums.DeliveryMethod;
import de.telran.gartenshop.entity.enums.OrderStatus;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long orderId;
    private Timestamp createdAt;
    private String deliveryAddress;
    private String contactPhone;
    private DeliveryMethod deliveryMethod;
    private OrderStatus orderStatus;
    private Timestamp updatedAt;

    @JsonProperty("items")
    private Set<OrderItemResponseDto> orderItemsSet;

    @JsonProperty("user")
    private UserResponseDto user;
}
