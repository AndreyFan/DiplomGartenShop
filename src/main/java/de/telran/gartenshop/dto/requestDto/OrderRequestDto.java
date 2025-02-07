package de.telran.gartenshop.dto.requestDto;

import de.telran.gartenshop.entity.enums.DeliveryMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {
    private String deliveryAddress;
    private DeliveryMethod deliveryMethod;
}
