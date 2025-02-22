package de.telran.gartenshop.dto.requestDto;

import de.telran.gartenshop.entity.enums.DeliveryMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {
    @NotNull(message = "Delivery address cannot be empty!")
    @Size(min = 1, max = 150, message = "Delivery address must be <= 150 characters")
    private String deliveryAddress;

    @NotNull(message = "Delivery method cannot be empty!")
    private DeliveryMethod deliveryMethod;
}
