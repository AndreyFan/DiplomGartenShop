package de.telran.gartenshop.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CanceledOrderDto {
    private Long orderId;
    private String deliveryAddress;
    private Timestamp deliveryAt;
    private int totalQuantity;
}
